import sys
from config_parser import ConfigParser

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print "Usage: python parse_config.py [node_config.cold] [original_config.xml]\n"
        exit()


    config = ConfigParser(sys.argv[1], sys.argv[2])
    print config.parse()