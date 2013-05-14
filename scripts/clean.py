import os
import shutil
import cold

def clean_paths():
    if os.path.exists(cold.NODES_DIR):
        print "Removing distributed configurations: " + cold.NODES_DIR
        shutil.rmtree(cold.NODES_DIR)

def clean_jvms():
    #TODO
    pass

if __name__ == "__main__":
    clean_paths()
    clean_jvms()