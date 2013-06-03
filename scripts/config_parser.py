from xml.dom import minidom
import os
from config.config import Config

class ConfigParser(object):

    def __init__(self, node_config):
        self.node_config = node_config
        self.node_map = {}
        self.url_map = {}
        self.new_xml_files = {}
        self.new_xml_content = {}
        self.beans_map = {}
        self.class_id = {}
        self.files_map = {}
        self.d = minidom.Document()
        self.location = ''
        self.skeleton_nodes = {}
        self.factory_nodes = {}
        self.first_node = ''

    def read_node_config(self):
        f = file(self.node_config)
        self.config = Config(f)
        self.original_xml = self.config.springConfiguration

        for node in self.config.beansDistribution:
            self.node_map[node.host] = node.beans

            for b in node.beans:
                self.url_map[b] = node.host

            file_name = self.create_file_for_node(node.host)
            node.file = file_name

        if self.config.main.standAlone:
            file_name = self.create_file_for_node(self.config.main.host)
            self.config.main.file = file_name
            self.node_map[self.config.main.host] = []

    def create_file_for_node(self, url):
        number = len(self.new_xml_files)
        (_, original_name) = os.path.split(self.original_xml)
        new_name = original_name[:-4] + str(number) + original_name[-4:]
        self.files_map[url] = new_name
        self.new_xml_files[url] = open(self.location+new_name, mode='w')
        self.new_xml_files[url].write('<?xml version="1.0" encoding="UTF-8"?>\n')
        self.new_xml_files[url].write('<!--' + url + '-->\n')

        return new_name

    def parse_xml(self):
        xml = minidom.parse(self.original_xml)
        beans = xml.getElementsByTagName('bean')
        xml_head = xml.getElementsByTagName('beans')
        xml_head[0].childNodes = []

        for bean in beans:
            id = bean.attributes['id'].value
            clazz = bean.attributes['class'].value
            self.beans_map[id] = bean
            self.class_id[id] = clazz
            print 'bean', bean.attributes['id'].value

        for url in self.new_xml_files:
            print url

            head = xml_head[0].cloneNode(False)

            application_context = self.create_application_context()

            head.appendChild(application_context)

            proxy_actor_system = self.create_proxy_actor_system(url)

            head.appendChild(proxy_actor_system)

            used_nodes = []

            for node in self.node_map[url]:
                print node
                xml_node = self.beans_map[node]
                print xml_node
                new_node = xml_node.cloneNode(True)
                self.check_references(url, new_node, used_nodes, head)
                head.appendChild(new_node)
                print used_nodes

            if self.config.main.host == url:
                self.create_main_config(used_nodes, head)

            self.new_xml_content[url] = head

    def add_skeletons(self):
        for url in self.new_xml_content:
            head = self.new_xml_content[url]
            for id in self.node_map[url]:
                if id in self.skeleton_nodes:
                    head.appendChild(self.skeleton_nodes[id])

    def create_main_config(self, used_nodes, head):
        for id in self.url_map:
            if id not in used_nodes and id not in self.node_map[self.config.main.host]:
                factory = self.create_factory_node(id, self.url_map[id], True)
                head.appendChild(factory.cloneNode(True))
                used_nodes.append(id)

    def check_references(self, url, xml_node, used_nodes, head):
        for property in xml_node.getElementsByTagName('property'):
            if property.attributes.has_key('ref'):
                id = property.attributes['ref'].nodeValue
                print id
                if id not in self.node_map[url]:
                    if id not in self.factory_nodes:
                        factory = self.create_factory_node(id, self.url_map[id])
                        skeleton = self.create_skeleton_node(id)
                        self.factory_nodes[id] = factory
                        self.skeleton_nodes[id] = skeleton

                    if id not in used_nodes:
                        print 'id', id
                        head.appendChild(self.factory_nodes[id].cloneNode(True))
                        used_nodes.append(id)

    def create_skeleton_node(self, id):
        child = self.d.createElement('bean')
        child.setAttribute('id', id + '.skeleton')
        child.setAttribute('class', 'pl.edu.agh.toik.cold.proxy.ProxySkeleton')

        child.appendChild(self.create_constructor_ref_arg('cold.proxyActorSystem'))
        child.appendChild(self.create_constructor_ref_arg(id))
        child.appendChild(self.create_constructor_arg(id))

        return child


    def create_factory_node(self, id, url, lazy_init = False):
        child = self.d.createElement('bean')
        child.setAttribute('id', id)
        child.setAttribute('class', 'pl.edu.agh.toik.cold.proxy.ProxyStubFactory')
        child.setAttribute('factory-method', 'createProxyStub')

        if lazy_init:
            child.setAttribute('lazy-init', 'true')

        child.appendChild(self.create_constructor_arg(self.class_id[id]))
        child.appendChild(self.create_constructor_arg(id))
        child.appendChild(self.create_constructor_ref_arg('cold.proxyActorSystem'))

        (host, port) = url.split(":")
        child.appendChild(self.create_constructor_arg(host))
        child.appendChild(self.create_constructor_arg(port))

        return child

    def create_constructor_ref_arg(self, value):
        carg = self.d.createElement('constructor-arg')
        ref = self.d.createElement('ref')
        ref.setAttribute('bean', value)
        carg.appendChild(ref)
        return carg

    def create_constructor_arg(self, value):
        carg = self.d.createElement('constructor-arg')
        val = self.d.createElement('value')
        text = self.d.createTextNode(value)
        val.appendChild(text)
        carg.appendChild(val)
        return carg

    def create_proxy_actor_system(self, url):
        child = self.d.createElement('bean')
        child.setAttribute('id', 'cold.proxyActorSystem')
        child.setAttribute('class', 'pl.edu.agh.toik.cold.proxy.ProxyActorSystem')

        (host, port) = url.split(":")
        child.appendChild(self.create_constructor_arg(host))
        child.appendChild(self.create_constructor_arg(port))

        return child

    def create_application_context(self):
        child = self.d.createElement('bean')
        child.setAttribute('id', 'cold.springApplicationContext')
        child.setAttribute('class', 'pl.edu.agh.toik.cold.utils.SpringApplicationContext')

        return child

    def close_xml(self):
        for url in self.new_xml_content:
            head = self.new_xml_content[url]
            xml = head.toprettyxml('\t')
            xml = ''.join([s for s in xml.splitlines(True) if s.strip()])

            self.new_xml_files[url].write(xml)

        for file in self.new_xml_files.values():
            file.close()

    def parse(self):
        self.read_node_config()
        self.parse_xml()
        self.add_skeletons()
        self.close_xml()
        return self.config