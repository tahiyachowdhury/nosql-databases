# Put the use case you chose here. Then justify your database choice:
# Use case: Instagram. I chose neo4j for this use case because I believe a graph based model with nodes and relationships is the best choice for a social networking app. An instagram app won't require top-level searches as well. We can easily branch out from one node.  

# Explain what will happen if coffee is spilled on one of the servers in your cluster, causing it to go down.
# Since I will be using Neo4j high availability cluster, other instances in the cluster will detect that and mark it as temporarily failed. When it becomes avialable, it will automatically catch up with the cluster. If the master goes down, one of the other instances will take over. 

# What data is it not ok to lose in your app? What can you do in your commands to mitigate the risk of lost data?
# It is not okay to lose data such as usernames and the pictures posted by users. I can mitigate the risk of lost data by writing atomic commands and have backup of my database. I can also prevent the complete delition of certain attributes while creating the schema.  

from neo4j.v1 import GraphDatabase
uri = "bolt://localhost:7687"
driver = GraphDatabase.driver(uri, auth=("neo4j", "test"))
session = driver.session()
# Action 1: User creates an account
session.run("CREATE (n:User4 {username: 'Max', folllowers:0})")

# Action 2: A user sees all the photos of the people they follow from the last week
result1 = session.run("MATCH (:User1)-[:Follows]->(:User2)-[r:Posted]->(m) WHERE r.Date > 20180424 RETURN m")
print "Action 2"
for x in result1:
   print x

# Action 3: A user sees all the photos of one particular person they follow
print "Action 3"
result2 = session.run("MATCH (:User1)-[:Follows]->(:User2)-[:Posted]->(m) RETURN m")
for x in result2:
   print x

# Action 4: A user follows another user
print "Action 4"
session.run("MATCH (a:User1),(b:User3) CREATE (a)-[r:Follows]->(b) RETURN type(r);")
session.run("MATCH (n:User3) SET n.followers = n.followers + 1 RETURN n;")
result4 = session.run("MATCH (n:User4)-[r:Follows]->(m) RETURN n,r,m;")
for x in result4:
    print x

# Action 5: A user comments on another's photo
print "Action 5"
session.run("CREATE (n:Comment8 {text: 'Miss you!'});")
session.run("MATCH (a:User1),(b:Comment8) CREATE (a)-[r:Commented]->(b) RETURN type(r);")
session.run("MATCH (a:Comment8),(b:Photo6) CREATE (a)-[r:On]->(b) RETURN type(r);")
result5 = session.run("MATCH (n)-[r:On]->(m:Photo6) RETURN n,r,m;")
for x in result5:
    print x

# Action 6: A user posts a picture
print "Action 6"
session.run("CREATE(n:Photo9 {caption: 'New on the insta!!'});")
session.run("MATCH (a:User4),(b:Photo9) CREATE (a)-[r:Posted {Date: 20180501}]->(b) RETURN type(r);")
result6 = session.run("MATCH (n:User4)-[r:Posted]->(m) RETURN n,r,m;")
for x in result6:
    print x

# Action 7: A user deletes a picture
print "Action 7"
session.run("MATCH (:User2)-[:Posted]->(m:Photo6) DETACH DELETE m;")
result7 = session.run("MATCH (n:User2)-[r:Posted]->(m) RETURN n,r,m;")
for x in result7:
    print x

# Action 8: A user deletes their comment from a picture
print "Action 8"
session.run("MATCH (:User1)-[:Commented]->(m:Comment4)  DETACH DELETE m;")
result8 = session.run("MATCH (n:User1)-[r:Commented]->(m) RETURN n,r,m;")
for x in result8:
    print x



