#!/usr/bin/env python

import requests



url = "https://api.nasa.gov/planetary/apod?api_key=SnWx4EGs8U60WNRt1btT9JeDKvyiUOkxR3nuTaSO&date=2017-09-18"



response = requests.get(url)



#Parsing the response text using json

res_data = response.json()



print(res_data['url'])
