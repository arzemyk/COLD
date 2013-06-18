import os
import pickle
from subprocess import Popen
import sys
import shutil
from time import sleep
import clean
from config_parser import ConfigParser
import exception


NODES_DIR = 'nodes'
SKELETON_CLASS = 'pl.edu.agh.toik.cold.runner.SkeletonRunner'

CONF_TEMP_FILENAME = 'conf.temp'

def get_port(address):
    return address.split(':')[1]


def create_node(node):
    dir_name = NODES_DIR + os.sep + get_port(node.host)
    springConfFile = node.file
    os.mkdir(dir_name)
    shutil.move(springConfFile, dir_name + os.sep + springConfFile)

def distribute(conf):
    os.mkdir(NODES_DIR)
    for node in conf.beansDistribution:
        create_node(node)

    if conf.main.standAlone:
        create_node(conf.main)

def run_nodes(conf):

    for node in conf.beansDistribution:
        address = node.host
        port = get_port(address)
        classpath = '-cp ' + conf.classPath + ';' + conf.coldLocation + ';' + NODES_DIR + os.sep + port
        print(classpath)

        command_template = 'java ' + classpath + ' %s ' + node.file

        if address == conf.main.host:
            main_command = command_template % conf.main.className
            continue
        command = command_template % SKELETON_CLASS
        proc = Popen(command)
        sleep(5)

    if conf.main.standAlone:
        classpath = '-cp ' + conf.classPath + ';' + conf.coldLocation + ';' + NODES_DIR + os.sep + get_port(conf.main.host)
        main_command = 'java ' + classpath + ' ' + conf.main.className + ' ' + conf.main.file

    main_proc = Popen(main_command)
    sleep(10)

def save_temp_config(conf):
    output = open(CONF_TEMP_FILENAME, 'wb')
    pickle.dump(conf, output)

def read_temp_config():
    if os.path.isfile(CONF_TEMP_FILENAME):
        input_s = open(CONF_TEMP_FILENAME, 'rb')
        return pickle.load(input_s)
    return None

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print "Usage: python cold.py [node_config.cold]\n"
        exit()

    clean.clean_paths()

    parser = ConfigParser(sys.argv[1])
    try:
        conf = parser.parse()

        distribute(conf)
        run_nodes(conf)
    except exception.ValidationException, e:
        print 'Configuration error: ' + e.value

