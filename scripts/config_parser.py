from xml.dom import minidom
import os

class ConfigParser(object):

    def __init__(self, node_config, original_xml):
        self.original_xml = original_xml
        self.node_config = node_config
        self.node_map = {}
        self.new_xml = {}
        self.beans_map = {}
        self.class_id = {}
        self.files_map = {}
        self.d = minidom.Document()
        self.location = ''

    def read_node_config(self):
        config = open(self.node_config, mode='r')

        content = config.readlines()
        for line in content:
            [url, beans] = line.split(';')
            beans = [b.strip() for b in beans.split(',')]
            url = url.strip()
            self.node_map[url] = beans
            self.create_file_for_node(url)

        config.close()

    def create_file_for_node(self, url):
        number = len(self.new_xml)
        (_, original_name) = os.path.split(self.original_xml)
        new_name = original_name[:-4] + str(number) + original_name[-4:]
        self.files_map[url] = new_name
        self.new_xml[url] = open(self.location+new_name, mode='w')
        self.new_xml[url].write('<!--' + url + '-->\n')
        self.new_xml[url].write('<?xml version="1.0" encoding="UTF-8"?>\n')

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

        self.ref_nodes = {}
        for url in self.new_xml:
            print url
            head = xml_head[0].cloneNode(False)

            used_nodes = []

            for node in self.node_map[url]:
                print node
                xml_node = self.beans_map[node]
                print xml_node
                new_node = xml_node.cloneNode(True)
                self.check_references(url, new_node, used_nodes, head)
                head.appendChild(new_node)
                print used_nodes

            head.writexml(self.new_xml[url], '', '\t', '\n')

    def check_references(self, url, xml_node, used_nodes, head):
        for property in xml_node.getElementsByTagName('property'):
            if property.attributes.has_key('ref'):
                id = property.attributes['ref'].nodeValue
                print id
                if id not in self.node_map[url]:
                    if id not in self.ref_nodes:
                        node = self.create_factory_node(id)
                        self.ref_nodes[id] = node

                    if id not in used_nodes:
                        print 'id', id
                        head.appendChild(self.ref_nodes[id].cloneNode(True))
                        used_nodes.append(id)

    def create_factory_node(self, id):
        child = self.d.createElement('bean')
        child.setAttribute('id', id)
        child.setAttribute('class', 'pl.edu.agh.toik.cold.proxy.ProxyStubFactory')
        child.setAttribute('factory-method', 'createProxyStub')

        child.appendChild(self.create_constructor_arg(self.class_id[id]))
        child.appendChild(self.create_constructor_arg(id))

        return child

    def create_constructor_arg(self, value):
        carg = self.d.createElement('constructor-arg')
        val = self.d.createElement('value')
        text = self.d.createTextNode(value)
        val.appendChild(text)
        carg.appendChild(val)
        return carg

    def close_xml(self):
        for file in self.new_xml.values():
            file.close()

    def parse(self):
        self.read_node_config()
        self.parse_xml()
        self.close_xml()
        return self.files_map