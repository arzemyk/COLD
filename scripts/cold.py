import os
from subprocess import Popen
import sys
import shutil
import clean
from config_parser import ConfigParser

NODES_DIR = 'nodes'
SKELETON_CLASS = 'pl.edu.agh.toik.cold.skeleton.SkeletonMock'
EXAMPLE_CLASS = 'pl.edu.agh.toik.cold.example.ExampleDemo'
COLD_CLASSPATH = '..' + os.sep + 'target' +os.sep + 'classes'
DETACHED_PROCESS = 0x00000008

def get_port(address):
    return address.split(':')[1]

def distribute(conf):
    os.mkdir(NODES_DIR)
    for address in conf:
        dir_name = NODES_DIR + os.sep + get_port(address)
        file_name = conf[address]
        os.mkdir(dir_name)
        shutil.move(file_name, dir_name + os.sep + file_name)

def run_nodes(conf):
    for address in conf:
        port = get_port(address)
        classpath = '-cp ' + COLD_CLASSPATH + ';' + NODES_DIR + os.sep + port
        print(classpath)
        proc = Popen('java ' + classpath + ' ' + SKELETON_CLASS + ' ' + port)
        print proc.stderr
        print proc.stdout

    # classpath = '-cp ' + COLD_CLASSPATH + ';' + NODES_DIR + os.sep + '10004'
    # proc = Popen('java ' + classpath + ' ' + EXAMPLE_CLASS  + ' ' + '10004')
    # print proc.stdout
    # print proc.stderr


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print "Usage: python cold.py [node_config.cold] [original_config.xml]\n"
        exit()


    parser = ConfigParser(sys.argv[1], sys.argv[2])
    configuration = parser.parse()
    distribute(configuration)
    run_nodes(configuration)