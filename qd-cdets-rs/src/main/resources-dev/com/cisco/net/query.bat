query=$1
date0=$2
date1=$3

query.pl "$query" | table.pl $date0 $date1 -daily -closed CJUDFMRV out inc closed