import os
import shutil
import cold


if os.path.exists(cold.NODES_DIR):
    print "Removing distributed configurations: " + cold.NODES_DIR
    shutil.rmtree(cold.NODES_DIR)
