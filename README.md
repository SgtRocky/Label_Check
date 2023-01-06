# Label_Check

Program created to check a master label against accompanying packing labels.

The User ID has a test where the prefix must contain HL and 8 symbols example: L008668
The master label has a 2 letter prefix, example FM123 (prefix editable in config.txt)
The accompanying labels have no prefix

The software requires two configuration files
Config.txt - Defining COM port on the computer for the label printer and the prefix to the master label
lblconfig.txt - Defining the label design using IPL (ZPL or other printer languages can be used)

Software writes a log in the log.txt file with
