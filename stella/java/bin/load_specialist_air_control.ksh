#!/usr/bin/ksh
. /home/dw/bin/set_oracle_variables.ksh
dbase_id=DWLIVE
home="/home/dw/"$dbase_id                     #  home path
stella_path=$home"/stella"                  #  Path for the app files
# run main mapping script and redirect output to date-stamped log file
$stella_path/load_specialist_air.ksh  >$home/logs/stella/load_specialist_air`date +%Y%m%d`.log 2 >>$home/logs/stella/load_specialist_air`date +%Y%m%d`.log
