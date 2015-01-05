import pprint

__author__ = 'Joao'

import shutil
from os.path import isfile

from rdflib import Graph, Namespace, RDF, Literal, BNode, URIRef, XSD

import json
from pprint import pprint

g = Graph()
if(not isfile("output.ttl")):
	shutil.copy2('onto.ttl','output.ttl')

g.parse("output.ttl", format="n3")

print("graph has %s statements.\n" % len(g))

ns=Namespace('http://www.movierecomendation.pt/ontology/movierecomendation.owl#')

firstYear = 2004
lastYear = 2004
thisYear = firstYear

while thisYear <= lastYear:
	filename = 'movies_' + str(thisYear) + '.json'
	#load all json files to store in RDF
	FileData = open(filename)
	MovieJSON = json.load(FileData)
	FileData.close()

	filename = 'series_' + str(thisYear) + '.json'
	#load all json files to store in RDF
	FileData = open(filename)
	SeriesJSON = json.load(FileData)
	FileData.close()

	filename = 'persons_' + str(thisYear) + '.json'
	FileData = open(filename)
	PersonJSON = json.load(FileData)
	FileData.close()

	filename = 'studios_' + str(thisYear) + '.json'
	FileData = open(filename)
	StudioJSON = json.load(FileData)
	FileData.close()

	#
	# STORE PPL IN TREE
	#########################

	LocalNamespace=Namespace('http://www.movierecomendation.pt/Person/')
	for person in PersonJSON:

		#see if Person is already added
		search = g.value(predicate=ns.hasPersonId, object=Literal(person['id']))
		if(search!=None):
			pprint("Error:Theres already an Instance for the Movie id " + person['id'])
			continue;

		PersonNode = URIRef(LocalNamespace[person['id']]);

		g.add((PersonNode,RDF.type,ns.Person))

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
				for prof in person[attr]:
					g.add((PersonNode,ns.hasProfession,ns[prof.replace(" ","_")]))
					pprint(ns[prof.replace(" ","_")])
			if(attr=='birthPlace'):
				g.add((PersonNode,ns.hasPersonBirthPlace,Literal(person[attr])))
			if(attr=='birthDate'):
				g.add((PersonNode,ns.hasPersonBirth,Literal(person[attr], datatype=XSD.date)))
			#if(attr=='actor'):
			#	pprint(person[attr])
			#if(attr=='director'):
			#	pprint(person[attr])

	#
	# STORE STUDIOS IN TREE
	#########################

	LocalNamespace=Namespace('http://www.movierecomendation.pt/Studio/')
	for studio in StudioJSON:
		#see if Studio is already added
		search = g.value(predicate=ns.hasStudioId,object=Literal(studio['id']))
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
		search = g.value(predicate=ns.hasMediaId,object=Literal(movie['id']))
		if(search!=None):
			pprint("Error:Theres already an Instance for the Movie id "+movie['id'])
			continue;

		MovieNode = URIRef(LocalNamespace[movie['id']]);
		g.add((MovieNode,RDF.type,ns.Movie))

		for attr in movie:
			if(attr=='id'):
				g.add((MovieNode,ns.hasMediaId,Literal(movie[attr])))
			if(attr=='directors'):
				for director in movie[attr]['director']:
					search = g.value(predicate=ns.hasPersonId, object=Literal(director))
					if(search==None):
						pprint("Error:Director not found")
						continue;
					g.add((MovieNode,ns.hasDirector,search))
			if(attr=='studios'):
				for studio in movie[attr]['studio']:
					search = g.value(predicate=ns.hasStudioId, object=Literal(studio))
					if(search==None):
						pprint("Error:Studio not found")
						continue;
					g.add((MovieNode,ns.hasStudio,search))
			if(attr=='launchDate'):
				g.add((MovieNode,ns.hasMediaLaunchDate,Literal(movie[attr], datatype=XSD.date)))
			if(attr=='genres'):
				for genre in movie[attr]['genre']:
					g.add((MovieNode,ns.hasGenre,ns[genre]))
			if(attr=='description'):
				g.add((MovieNode,ns.hasMediaDescription,Literal(movie[attr])))
			if(attr=='score'):
				g.add((MovieNode,ns.hasMediaClassification,Literal(movie[attr], datatype=XSD.float)))
			if(attr=='duration'):
				g.add((MovieNode,ns.hasMediaDuration,Literal(movie[attr], datatype=XSD.integer)))
			if(attr=='stars'):
				for star in movie[attr]['star']:
					search = g.value(predicate=ns.hasPersonId,object=Literal(star))
					if(search==None):
						pprint("Error:Star not found")
						continue;
					g.add((MovieNode,ns.hasActor,search))
			if(attr=='name'):
				g.add((MovieNode,ns.hasMediaName,Literal(movie[attr])))
			if(attr=='image'):
				g.add((MovieNode,ns.hasMediaPoster,Literal(movie[attr])))

	#
	# STORE SERIES IN TREE
	#########################

	LocalNamespace=Namespace('http://www.movierecomendation.pt/Serie/')

	for serie in SeriesJSON:
		#see if movie is already added
		search = g.value(predicate=ns.hasMediaId,object=Literal(serie['id']))
		if(search!=None):
			pprint("Error:Theres already an Instance for the Serie id "+serie['id'])
			continue;

		SerieNode = URIRef(LocalNamespace[serie['id']]);
		g.add((SerieNode,RDF.type,ns.Serie))

		for attr in serie:
			if(attr=='id'):
				g.add((SerieNode,ns.hasMediaId,Literal(serie[attr])))
			if(attr=='studios'):
				for studio in serie[attr]['studio']:
					search = g.value(predicate=ns.hasStudioId, object=Literal(studio))
					if(search==None):
						pprint("Error:Studio not found")
						continue;
					g.add((SerieNode,ns.hasStudio,search))
			if(attr=='start'):
				g.add((SerieNode,ns.hasSerieStart,Literal(serie[attr], datatype=XSD.integer)))
			if(attr=='end'):
				if(serie[attr] != 0):
					g.add((SerieNode,ns.hasSerieEnd,Literal(serie[attr], datatype=XSD.integer)))
			if(attr=='genres'):
				for genre in serie[attr]['genre']:
					g.add((SerieNode,ns.hasGenre,ns[genre]))
			if(attr=='description'):
				g.add((SerieNode,ns.hasMediaDescription,Literal(serie[attr])))
			if(attr=='score'):
				g.add((SerieNode,ns.hasMediaClassification,Literal(serie[attr], datatype=XSD.float)))
			if(attr=='duration'):
				g.add((SerieNode,ns.hasMediaDuration,Literal(serie[attr], datatype=XSD.integer)))
			if(attr=='stars'):
				for star in serie[attr]['star']:
					search = g.value(predicate=ns.hasPersonId,object=Literal(star))
					if(search==None):
						pprint("Error:Star not found")
						continue;
					g.add((SerieNode,ns.hasActor,search))
			if(attr=='name'):
				g.add((SerieNode,ns.hasMediaName,Literal(serie[attr])))
			if(attr=='image'):
				g.add((SerieNode,ns.hasMediaPoster,Literal(serie[attr])))
			if(attr=='seasons'):
				g.add((SerieNode,ns.hasSerieSeason,Literal(serie[attr], datatype=XSD.integer)))



	thisYear += 1

thisYear = firstYear
while thisYear <= lastYear:

	filename = 'persons_' + str(thisYear) + '.json'
	FileData = open(filename)
	PersonJSON = json.load(FileData)
	FileData.close()

	LocalNamespace=Namespace('http://www.movierecomendation.pt/Person/')
	for person in PersonJSON:
		PersonNode = URIRef(LocalNamespace[person['id']]);
		search = g.value(predicate=ns.hasPersonId, object=Literal(person['id']))
		if(search == None):
			pprint("Error: Person not found! " + person['id'])
			continue;
		try:
			for relevant in person['knownFor']:
				search = g.value(predicate=ns.hasMediaId, object=Literal(relevant))
				if search == None:
					pprint("Error: this better be a series: " + relevant)
					continue
				g.add((PersonNode,ns.isKnownFor,search))
		except (Exception ):
			pprint("Error: attribute not found on " + person['id'])	

	thisYear += 1

g.serialize(destination='output1.ttl', format='n3')