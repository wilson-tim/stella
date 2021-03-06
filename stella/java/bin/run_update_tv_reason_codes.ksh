#!/bin/ksh
#set -x

#############################################################################################
#
# 
#
#--------------------------------------------------------------------------------------------
# Change
# ------
#
# Date          Who     Description
# ----          ---     -----------
# 10/10/2003   Leigh     Created script
#
#############################################################################################
# Input Parameters
# ----------------
#
# None
#
############################################################################################


function report
{
   echo `date +%Y/%m/%d---%H:%M:%S` $*
}

appdir="/home/dw/DWLIVE/stella"
logdir="/home/dw/SQL_Output"
today_d=`date +%Y%b%d`
today_d=`echo $today_d | sed /.*/y/ADFJMNOS/adfjmnos/`
PASSWD=`cat /home/dw/DWLIVE/passwords/stellabatch.txt`
echo $PASSWD 

report "--------------------------------------------------------------------------"
report ">>>>>> Stella upate TV bookings"
report "--------------------------------------------------------------------------"

# Setting variables
report "Setting variables"
. /home/dw/bin/set_oracle_variables.ksh

logfile=$appdir/run_update_tv_reason_codes.lst

error_mail_list="${appdir}/appsupportmail.lst"
user_mail_list="${appdir}/usermail.lst"

report "Run the stored proc that runs the report"
sqlplus  stellabatch/$PASSWD  @$appdir/run_update_tv_reason_codes.sql > $logfile

report "display logfile contents:"
cat $logfile

report "Test for errors"
echo "Logfile:" $logfile
grep "Fail" $logfile > $appdir/miss_error.err 
grep "Error " $logfile >> $appdir/miss_error.err
grep "ERROR" $logfile >> $appdir/miss_error.err
grep "error " $logfile >> $appdir/miss_error.err
grep "SEVERE" $logfile >> $appdir/miss_error.err
grep "WARNING" $logfile >> $appdir/miss_error.err
grep "CRITICAL" $logfile >> $appdir/miss_error.err
# -s tests that size > 0 bytes
report "Need error email?"
if [ -s $appdir/miss_error.err ]
 then
   echo "error found"
   echo >>  $appdir/miss_error.err
   cat ${error_mail_list}|while read users
   do
     echo ${users}
     mailx -s "Details of `hostname` STELLA updated TV reason codes errors encountered  on "$today_d" " ${users} <  $appdir/miss_error.err
   done


fi


report "output summary"
echo "Logfile:" $logfile
grep "reas" $logfile > $appdir/miss_error.err 
grep "Count" $logfile >> $appdir/miss_error.err 
# -s tests that size > 0 bytes
report "Need summ email?"
if [ -s $appdir/miss_error.err ]
 then
   echo "summ found"
   echo >>  $appdir/miss_error.err
   cat ${user_mail_list}|while read users
   do
     echo ${users}
     mailx -s "Summary of `hostname` STELLA updated TV reason codes on "$today_d" " ${users} <  $appdir/miss_error.err
   done
fi
report "END OF PROGRAM" 
exit 0
