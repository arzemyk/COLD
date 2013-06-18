import os
from validator import Validator
from xml.dom import minidom
from config.config import Config
from xml_node_generator import XMLNodeGenerator

class ConfigParser(object):

    def __init__(self, node_config):
        self.node_config = node_config
        self.url_to_bean_map = {}
        self.bean_to_url_map = {}
        self.new_xml_files = {}
        self.new_xml_content = {}
        self.xml_beans_node_map = {}
        self.url_to_files_map = {}
        self.location = ''
        self.skeleton_nodes = {}
        self.factory_nodes = {}

    # reads config from .cold file
    def read_node_config(self):
        f = file(self.node_config)
        self.config = Config(f)

        Validator.validate(self.config)
        return self.config

    # creates new file for given url
    def create_file_for_node(self, url):
        number = len(self.new_xml_files)
        (_, original_name) = os.path.split(self.config.springConfiguration)
        new_name = original_name[:-4] + str(number) + original_name[-4:]
        self.url_to_files_map[url] = new_name
        self.new_xml_files[url] = open(self.location+new_name, mode='w')
        self.new_xml_files[url].write('<?xml version="1.0" encoding="UTF-8"?>\n')
        self.new_xml_files[url].write('<!--' + url + '-->\n')

        return new_name

    # creates stubs and skeletons for all beans
    def create_stubs_and_skeletons(self):
        for id in self.bean_to_url_map:
            factory = self.xml_node_generator.create_factory_node(id, self.bean_to_url_map[id])
            skeleton = self.xml_node_generator.create_skeleton_node(id)
            self.factory_nodes[id] = factory
            self.skeleton_nodes[id] = skeleton

    # preprocesses beans to extract bean ids and class names
    def preprocess(self, beans):
        for node in self.config.beansDistribution:
            self.url_to_bean_map[node.host] = node.beans

            for b in node.beans:
                self.bean_to_url_map[b] = node.host

            file_name = self.create_file_for_node(node.host)
            node.file = file_name

        if self.config.main.standAlone:
            file_name = self.create_file_for_node(self.config.main.host)
            self.config.main.file = file_name
            self.url_to_bean_map[self.config.main.host] = []

        class_id = {}
        for bean in beans:
            id = bean.attributes['id'].value
            clazz = bean.attributes['class'].value
            self.xml_beans_node_map[id] = bean
            class_id[id] = clazz
            print 'bean', bean.attributes['id'].value

        self.xml_node_generator = XMLNodeGenerator(class_id)

        self.create_stubs_and_skeletons()

    # creates application context and proxy actor system nodes
    def init_file(self, head, url):
        application_context = self.xml_node_generator.create_application_context()
        proxy_actor_system = self.xml_node_generator.create_proxy_actor_system(url)

        head.appendChild(application_context)
        head.appendChild(proxy_actor_system)

    # adds stubs to xml for main node
    def create_main_config(self, used_nodes, head):
        for id in self.bean_to_url_map:
            if id not in used_nodes and id not in self.url_to_bean_map[self.config.main.host]:
                head.appendChild(self.factory_nodes[id].cloneNode(True))
                used_nodes.append(id)

    # checks references for given bean and adds stub factory to node if necessary
    def check_references(self, url, xml_node, used_nodes, head):
        for property in xml_node.getElementsByTagName('property'):
            if property.attributes.has_key('ref'):
                id = property.attributes['ref'].nodeValue
                print id
                if id not in self.url_to_bean_map[url]:
                    if id not in used_nodes:
                        print 'id', id
                        head.appendChild(self.factory_nodes[id].cloneNode(True))
                        used_nodes.append(id)

    # parses xml configuration
    def parse_xml(self):
        xml = minidom.parse(self.config.springConfiguration)
        beans = xml.getElementsByTagName('bean')
        xml_head = xml.getElementsByTagName('beans')
        xml_head[0].childNodes = []

        self.preprocess(beans)

        for url in self.new_xml_files:
            print url

            head = xml_head[0].cloneNode(False)

            self.init_file(head, url)

            used_nodes = []
            for node in self.url_to_bean_map[url]:
                print node
                xml_node = self.xml_beans_node_map[node]
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
            for id in self.url_to_bean_map[url]:
                if id in self.skeleton_nodes:
                    head.appendChild(self.skeleton_nodes[id])

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