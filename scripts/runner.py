import sys
from config_parser import ConfigParser

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print "Usage: python parse_config.py [node_config.cold]\n"
        exit()


    config = ConfigParser(sys.argv[1])
    print config.parse()