import pprint

__author__ = 'Joao'

import shutil
from os.path import isfile

from rdflib import Graph, Namespace, RDF, Literal,BNode,URIRef
import json
from pprint import pprint



#load all json files to store in RDF
FileData = open('movies_1995.json')
MovieJSON = json.load(FileData)
FileData.close()

FileData = open('persons_1995.json')
PersonJSON = json.load(FileData)
FileData.close()

FileData = open('studios_1995.json')
StudioJSON = json.load(FileData)
FileData.close()

g = Graph()
if(not isfile("output.ttl")):
    shutil.copy2('onto.ttl','output.ttl')

g.parse("output.ttl", format="n3")

print("graph has %s statements.\n" % len(g))

ns=Namespace('http://www.movierecomendation.pt/ontology/movierecomendation.owl#')

#
# STORE PPL IN TREE
#########################

LocalNamespace=Namespace('http://www.movierecomendation.pt/Person/')
for person in PersonJSON:

    #see if Person is already added
    search = g.value(predicate=ns.HasPersonId, object=Literal(person['id']))
    if(search!=None):
        pprint("Error:Theres already an Instance for the Movie id "+person['id'])
        continue;

    PersonNode = URIRef(LocalNamespace[person['id']]);

    if(person['actor']):
        g.add((PersonNode,RDF.type,ns.Actor))
    else:
        g.add((PersonNode,RDF.type,ns.Director))

    for attr in person:
        if(attr=='id'):
            g.add((PersonNode,ns.hasPersonId,Literal(person[attr])))
        if(attr=='name'):
            g.add((PersonNode,ns.hasPersonName,Literal(person[attr])))
        if(attr=='picture'):
            g.add((PersonNode,ns.hasPersonPicture,Literal(person[attr])))
        if(attr=='miniBio'):
            g.add((PersonNode,ns.hasPersonMiniBio,Literal(person[attr])))
        if(attr=='jobCategories'):
            pprint(person[attr])
        if(attr=='birthPlace'):
            g.add((PersonNode,ns.hasPersonBirthPlace,Literal(person[attr])))
        if(attr=='birthDate'):
            g.add((PersonNode,ns.hasPersonBirth,Literal(person[attr])))
        if(attr=='actor'):
            pprint(person[attr])
        if(attr=='director'):
            pprint(person[attr])

#
# STORE STUDIOS IN TREE
#########################

LocalNamespace=Namespace('http://www.movierecomendation.pt/Studio/')
for studio in StudioJSON:
    #see if Studio is already added
    search = g.value(predicate=ns.HasStudioId,object=Literal(studio['id']))
    if(search!=None):
        pprint("Error:Theres already an Instance for the Studio id "+studio['id'])
        continue;

    StudioNode = URIRef(LocalNamespace[studio['id']]);
    g.add((StudioNode,RDF.type,ns.Studio))
    for attr in studio:
        if(attr=='id'):
            g.add((StudioNode,ns.hasStudioId,Literal(studio[attr])))
        if(attr=='name'):
            g.add((StudioNode,ns.hasStudioName,Literal(studio[attr])))

#
# STORE MOVIES IN TREE
#########################

LocalNamespace=Namespace('http://www.movierecomendation.pt/Movie/')

for movie in MovieJSON:
    #see if movie is already added
    search = g.value(predicate=ns.hasMovieId,object=Literal(movie['id']))
    if(search!=None):
        pprint("Error:Theres already an Instance for the Movie id "+movie['id'])
        continue;

    MovieNode = URIRef(LocalNamespace[movie['id']]);
    g.add((MovieNode,RDF.type,ns.Movie))

    for attr in movie:
        if(attr=='id'):
            g.add((MovieNode,ns.hasMovieId,Literal(movie[attr])))
        if(attr=='directors'):
            for director in movie[attr]['director']:
                search = g.value(predicate=ns.hasPersonId,object=Literal(director))
                if(search==None):
                    pprint("Error:Director not found")
                    continue;
                g.add((MovieNode,ns.hasDirector,search))
        if(attr=='studios'):
            for studio in movie[attr]['studio']:
                search = g.value(predicate=ns.hasStudioId,object=Literal(studio))
                if(search==None):
                    pprint("Error:Studio not found")
                    continue;
                g.add((MovieNode,ns.hasStudio,search))
        if(attr=='launchDate'):
            g.add((MovieNode,ns.hasMovieLaunchDate,Literal(movie[attr])))
        if(attr=='genres'):
            pprint(movie[attr])
            for genre in movie[attr]['genre']:
                g.add((MovieNode,ns.hasGenres,ns[genre]))
            #g.add((MovieNode,ns.HasMovieGenres,Literal(movie[attr])))
        if(attr=='description'):
            g.add((MovieNode,ns.hasMovieDescription,Literal(movie[attr])))
        if(attr=='score'):
            g.add((MovieNode,ns.hasMovieClassification,Literal(movie[attr])))
        if(attr=='duration'):
            g.add((MovieNode,ns.hasMovieDuration,Literal(movie[attr])))
        if(attr=='stars'):
            for star in movie[attr]['star']:
                search = g.value(predicate=ns.hasPersonId,object=Literal(star))
                if(search==None):
                    pprint("Error:Star not found")
                    continue;
                g.add((MovieNode,ns.hasActor,search))
        if(attr=='name'):
            g.add((MovieNode,ns.hasMovieName,Literal(movie[attr])))
        if(attr=='image'):
            g.add((MovieNode,ns.hasMoviePoster,Literal(movie[attr])))

g.serialize(destination='output1.ttl', format='n3')