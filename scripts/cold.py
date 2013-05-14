import os
from subprocess import Popen
import sys
import shutil
from time import sleep
import clean
from config_parser import ConfigParser


NODES_DIR = 'nodes'
SKELETON_CLASS = 'pl.edu.agh.toik.cold.runner.SkeletonRunner'
EXAMPLE_CLASS = 'pl.edu.agh.toik.cold.example.Main'
COLD_CLASSPATH = '..' + os.sep + 'target' +os.sep + 'cold-0.1-SNAPSHOT.jar'
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

def run_nodes(conf, main_node):

    for address in conf:
        port = get_port(address)
        classpath = '-cp ' + COLD_CLASSPATH + ';' + NODES_DIR + os.sep + port
        print(classpath)

        command_template = 'java ' + classpath + ' %s ' + conf[address]

        print address + " : " + main_node

        if address == main_node:
            main_command = command_template % EXAMPLE_CLASS
            continue
        command = command_template % SKELETON_CLASS
        proc = Popen(command)
        sleep(5)



    main_proc = Popen(main_command)
    sleep(5)




if __name__ == "__main__":
    if len(sys.argv) != 3:
        print "Usage: python cold.py [node_config.cold] [original_config.xml]\n"
        exit()

    clean.clean_paths()
    clean.clean_jvms()

    parser = ConfigParser(sys.argv[1], sys.argv[2])
    (main_node,configuration) = parser.parse()
    print main_node
    distribute(configuration)
    run_nodes(configuration, main_node.strip())