import os
import shutil
from subprocess import Popen
import sys
from time import sleep
import cold
from config_parser import ConfigParser

KILLER_CLASS = 'pl.edu.agh.toik.cold.runner.ColdKiller'

def clean_paths():
    if os.path.exists(cold.NODES_DIR):
        print "Removing distributed configurations: " + cold.NODES_DIR
        shutil.rmtree(cold.NODES_DIR)

def clean_jvms(conf):

    addresses = ' '.join([node.host for node in conf.beansDistribution])
    if conf.main.standAlone:
        addresses += ' ' + conf.main.host
    print addresses

    classpath = '-cp ' + conf.classPath + ';' + conf.coldLocation
    command = 'java ' + classpath + ' ' + KILLER_CLASS + ' ' + addresses
    print command
    proc = Popen(command)
    print "Cleaning jvms complete."
    sleep(10)




if __name__ == "__main__":
    if len(sys.argv) != 2:
        print "Usage: python cold.py [node_config.cold]\n"
        exit()

    parser = ConfigParser(sys.argv[1])
    conf = parser.parse()

    clean_paths()
    clean_jvms(conf)