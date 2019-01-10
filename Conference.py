import tweepy  # To consume Twitter's API
import json
from elasticsearch import Elasticsearch
# For plotting and visualization:
from config import *
import datefinder
import dateparser
import dateutil.parser as dparser
from datetime import datetime
import json
from watson_developer_cloud import NaturalLanguageUnderstandingV1
from watson_developer_cloud.natural_language_understanding_v1 import Features, RelationsOptions
import datetime


def twitter_setup():
    auth = tweepy.OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)
    auth.set_access_token(ACCESS_TOKEN, ACCESS_SECRET)

    # Return API with authentication:
    api = tweepy.API(auth)
    return api


natural_language_understanding = NaturalLanguageUnderstandingV1(
    version='2018-11-16',
    iam_apikey='dPxGfvre-tiq3PypTMJOpB-FQyE-mTZS_tkSVDKiBxWS',
    url='https://gateway-lon.watsonplatform.net/natural-language-understanding/api'
)

es = Elasticsearch([{'host': 'localhost', 'port': 9200}])
extractor = twitter_setup()
places = extractor.geo_search(query="France", granularity="country")
place_id = places[0].id
searchQuery = ' "conference de" OR "sous le theme" OR "seminaire"'
print(place_id)
maxTweets = 1000000
tweetsPerQry = 100
tweetCount = 0
print(extractor.rate_limit_status()['resources']['search'])
# Open a text file to save the tweets to
Id_enrg = 1
conferencejson = {}
conferencejson['conference'] = []

for tweet in tweepy.Cursor(extractor.search, q=searchQuery).items(maxTweets):
    if (len(tweet.text) > 0) and ("RT @" not in (tweet.text.upper())) and (
                "CONFERENCE DE PRESSE" not in (tweet.text.upper())) and (
                "CONFÉRENCE DE PRESSE" not in (tweet.text.upper())):
        person = ""
        location = ""
        try:
            response = natural_language_understanding.analyze(
                text=tweet.text,
                features=Features(relations=RelationsOptions())).get_result()
            for i in range(len(response['relations'])):
                for j in range(len(response['relations'][i]['arguments'])):
                    if response['relations'][i]['arguments'][j]['entities'][0]['type'] == "Person":
                        string_to_add = response['relations'][i]['arguments'][j]['entities'][0]['text']
                        if string_to_add == "j\u2019":
                            string_to_add = tweet._json['entities']['user_mentions'][0]['screen_name']
                        person += " " + string_to_add
                    elif response['relations'][i]['arguments'][j]['entities'][0]['type'] == "Location" or \
                                    response['relations'][i]['arguments'][j]['entities'][0][
                                        'type'] == "GeopoliticalEntity":
                        location = " " + response['relations'][i]['arguments'][j]['entities'][0]['text']
        except:
            continue

        try:
            date = datetime.datetime.min
            date_list = datefinder.find_dates(tweet.text.split('https')[0])
            for match in date_list:
                date = match
                break

        except ValueError:
            date = datetime.datetime.min
        if date > datetime.datetime.now() and int(date.year) < 2021:
            if date != "" or person != "" or location != "":
                hashtags = ""
                for elem in tweet._json['entities']['hashtags']:
                    hashtags += " " + elem['text']
                entry = {'tweet': tweet._json, 'date': str(date), 'location': location, 'Personnes': person,
                         'Hashtags': hashtags, 'text': tweet.text}
                conferencejson['conference'].append(entry)
                print(entry)
                es.index(index='conference', doc_type='conference', id=Id_enrg, body=entry)
                Id_enrg += 1

        
