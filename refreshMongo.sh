#!/bin/bash

db=local
collection=screens
mongopath="/c/Program Files/MongoDB/Server/3.6/bin"

cd bin/
java mm.MongoManager

cd ../output
ls -1 *.json | sed 's/.json$//' | while read col; do 
	if [ $col = "data_1" ]
	then
	    "$mongopath/mongoimport" --db $db --collection $collection --drop < $col.json; 
	else
	    "$mongopath/mongoimport" --db $db --collection $collection < $col.json; 
	fi
done
