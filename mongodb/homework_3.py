import pymongo
from pymongo import MongoClient

client = MongoClient()
database = client.local

collection = database.movies
collection2 = database.actor
collection3 = database.director
collection.update(
    {
        '$and': [
            { "rated": "NOT RATED"}, 
            { "genres": "documentary" }
        ]
    }, 
    { '$set': { "rated": "Pending rating" } }
)
cursor = collection.find({"genres":"documentary"})
print ("Part A")
for x in cursor:
    print (x)

collection.insert_one(
    {"title": "Icarus",
     "year": 2017,
     "countries": ["USA"],
     "genres": ["documentary", "Sport", "Thriller"],
     "rated": "NOT RATED",
     "directors": ["Bryan Fogel"],
     "imdb": {"rating": 8.0, "votes": 19210, "id": 25}})

cursor2 = collection.find({"title":"Icarus"})

print("Part B")
for x in cursor2:
    print (x)

cursor3 = collection.aggregate([
			{'$match': {"genres": "documentary"}},
			{'$group': {"_id":'$genres',"count":{'$sum': 1}}}
])
print("Part C")
for x in cursor3:
    print (x)

cursor4 = collection.aggregate([
                        {'$match': {'$and': [{"countries": "USA"}, {"rated": "Pending rating"}]}},
                       {'$group': {"_id": {"countries": "$countries", "rated": "$rated"}, "count": {'$sum': 1}}}
])
print("Part D")
for x in cursor4:
    print (x)

collection3.insert([
   {"movie" : "A Terrible Night", "name": "Georges Melies", "year":1896 },
   {"movie" : "Alice in Wonderland", "name": "Cecil Hepworth", "year":1903}])

collection2.insert([
   {"movie_title" : "A Terrible Night", "name": "Georges Melies", "year":1896 },
   {"movie_title" : "Alice in Wonderland", "name": "May Clark", "year":1903}])

cursor5 = collection3.aggregate([
	{
	 '$lookup': 
	    {
		'from': "actor",
		'localField': "movie",
		'foreignField': "movie_title",
		'as': "actors_docs"
	    }
	}
])

print ("Part E")
for x in cursor5:
    print (x)
