from random import *
import sys
import os
import linecache

def getSQL(amount):
	sqlScript = open('./db_scripts/populating_script.sql', 'w')
	amountOfCities = 3173958
	seed()
	sql = "INSERT INTO WEATHER (TEMPERATURE, COORDINATES, CITY) VALUES ("
	for i in range (0, amount):
		temp = "{0:.2f}".format(uniform(-50.0, 40.0))
		numOfCity = randint(0, amountOfCities)
		citiesLine = linecache.getline('./src/main/resources/cities.csv', numOfCity).rstrip().split(',')
		coords = '\'' + citiesLine[1] + ',' + citiesLine[2] + '\'' 
		sqlScript.write(sql + temp + ', ' + coords + ', \'' + citiesLine[0].encode('string_escape') + '\');' + os.linesep)
		
if __name__=='__main__':
	reload(sys)
	if len(sys.argv) != 2:
		print 'usage ./weather_sql_randomizer.py --amount=*amount of sqls needed*'
		sys.exit(1)

	amount = int(sys.argv[1][9:])
	getSQL(amount)