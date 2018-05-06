// Please run the data into the database by running 'cat final_project.cypher | cypher-shell -u neo4j -p test' from the vagrant shell 
CREATE (n:User1 { username: 'Ana', followers: 2 });
CREATE (n:User2 { username: 'Alex', followers: 4 });
CREATE (n:User3 { username: 'Mary', followers: 1 });
CREATE(n:Photo1 {caption: 'Had so much fun!'});
CREATE(n:Photo2 {caption: 'YOLO'});
CREATE(n:Photo3 {caption: 'Love NYC!'});
CREATE(n:Comment1 {text: 'So cute!'});
CREATE(n:Comment2 {text: 'Love you!'});
CREATE(n:Photo4 {caption: 'Inserts a clever caption'});
CREATE(n:Photo5 {caption: 'Happy birthday to me!'});
CREATE(n:Photo6);
CREATE(n:Comment3 {text: 'Goals!'});
CREATE(n:Comment4 {text: 'So beautiful!'});
CREATE(n:Comment5 {text: 'Happy birthday!'});
CREATE(n:Comment6 {text: 'You are the cutest.'});
CREATE(n:Photo7);
CREATE(n:Photo8 {caption: 'Brunch time!'});
CREATE(n:Comment7 {text: 'Have to hangout the next time you are here.'});

//Relations
MATCH (a:User1),(b:User2)
CREATE (a)-[r:Follows]->(b)
RETURN type(r);

MATCH (a:User2),(b:User3)
CREATE (a)-[r:Follows]->(b)
RETURN type(r);

MATCH (a:User3),(b:User2)
CREATE (a)-[r:Follows]->(b)
RETURN type(r);

MATCH (a:User2),(b:Photo1)
CREATE (a)-[r:Posted {Date:20180423}]->(b)
RETURN type(r);

MATCH (a:User2),(b:Photo2)
CREATE (a)-[r:Posted {Date:20180424}]->(b)
RETURN type(r);

MATCH (a:User3),(b:Photo3)
CREATE (a)-[r:Posted {Date:20180103}]->(b)
RETURN type(r);

MATCH (a:User2),(b:Photo4)
CREATE (a)-[r:Posted {Date:20180420}]->(b)
RETURN type(r);

MATCH (a:User2),(b:Photo5)
CREATE (a)-[r:Posted {Date:20180324}]->(b)
RETURN type(r);

MATCH (a:User2),(b:Photo6)
CREATE (a)-[r:Posted {Date:20180124}]->(b)
RETURN type(r);

MATCH (a:User3),(b:Photo7)
CREATE (a)-[r:Posted {Date:20180424}]->(b)
RETURN type(r);

MATCH (a:User3),(b:Photo8)
CREATE (a)-[r:Posted {Date:20180425}]->(b)
RETURN type(r);

MATCH (a:User1),(b:Comment1)
CREATE (a)-[r:Commented]->(b)
RETURN type(r);

MATCH (a:User3),(b:Comment2)
CREATE (a)-[r:Commented]->(b)
RETURN type(r);

MATCH (a:User1),(b:Comment5) //goes with photo 5
CREATE (a)-[r:Commented]->(b)
RETURN type(r);

MATCH (a:User2),(b:Comment3)
CREATE (a)-[r:Commented]->(b)
RETURN type(r);

MATCH (a:User1),(b:Comment4)
CREATE (a)-[r:Commented]->(b)
RETURN type(r);

MATCH (a:User2),(b:Comment6)
CREATE (a)-[r:Commented]->(b)
RETURN type(r);

MATCH (a:User2),(b:Comment7)
CREATE (a)-[r:Commented]->(b)
RETURN type(r);

MATCH (a:Comment1),(b:Photo1)
CREATE (a)-[r:On]->(b)
RETURN type(r);

MATCH (a:Commen2),(b:Photo2)
CREATE (a)-[r:On]->(b)
RETURN type(r);

MATCH (a:Comment3),(b:Photo4)
CREATE (a)-[r:On]->(b)
RETURN type(r);

MATCH (a:Comment4),(b:Photo6)
CREATE (a)-[r:On]->(b)
RETURN type(r);

MATCH (a:Comment5),(b:Photo5)
CREATE (a)-[r:On]->(b)
RETURN type(r);

MATCH (a:Comment6),(b:Photo7)
CREATE (a)-[r:On]->(b)
RETURN type(r);

MATCH (a:Comment7),(b:Photo8)
CREATE (a)-[r:On]->(b)
RETURN type(r);


