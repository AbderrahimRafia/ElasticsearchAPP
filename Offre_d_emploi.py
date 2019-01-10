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
searchQuery = ' "offre d\'emploi" OR "emploi CDI"'
print(place_id)
maxTweets = 1000000
tweetsPerQry = 100
tweetCount = 0
print(extractor.rate_limit_status()['resources']['search'])
# Open a text file to save the tweets to
Id_enrg = 1
emploiList = ['prof','Abatteur', 'Accessoiriste', 'Accompagnateu', 'Actuaire', 'Administrateur',
              'Agent de recherche de financement', 'Agent', 'Aide-cuisinier', 'Analyste financier', 'Animateur',
              'Architecte', 'Architecte paysagiste', 'Assistante périnatale', 'Auteur', 'Avocat', 'Biologiste',
              'Brasseur', 'Caissier', 'Caméraman', 'Camionneur', 'Chauffeur', 'Chef cuisinier', 'Chef de camp',
              'Chef de service', 'Chef machiniste', 'Comédien', 'Commis comptable', 'Commis de magasin', 'Comptable',
              'Concepteur', 'Concierge', 'Conseiller', 'Contremaître', 'Coordonnateur', 'Cuisinier', 'Danseur',
              'Directeur', 'Educateur', 'Formateur', 'Géographe', 'Gérant', 'Gestionnaire immobilier', 'Graphiste',
              'Guide-animateur', 'Illustrateur', 'Ingenieur', 'Infirmier', 'Infographiste', 'Ingénieur', 'Intervenant',
              'Jardinier', 'Journaliste', 'Libraire', 'Magasinier', 'Manutentionnaire', 'Marionnettiste', 'Marteleur',
              'Médecin', 'Meneur de jeu', 'Mesureur', 'Metteur en onde', 'Musicien', 'Nutritionniste', 'Opérateur',
              'Organisateur', 'Orthopédagogue', 'Ouvrier', 'Paramédical', 'Pâtissiers', 'Porteur', 'Producteur',
              'Professeur de musique', 'Projectionniste', 'Psycho-éducateur', 'Psychologue', 'Psychothérapeute',
              'Réalisateur', 'Régisseur', 'Répartiteur', 'Représentant', 'Responsable de l’animation',
              'Responsable de service de garde', 'Sauveteur', 'Secrétaire', 'Secrétaire-réceptionniste', 'Serveur',
              'Surveillant', 'Technicien', 'Thanatopracteur', 'Traducteur', 'Trieur', 'Valoriste', 'Vendeur',
              'Webmestre']
conferencejson = {}
conferencejson['conference'] = []

for tweet in tweepy.Cursor(extractor.search, q=searchQuery).items(maxTweets):
    if (len(tweet.text) > 0) and ("RT @" not in (tweet.text.upper())):
        location = ""
        poste = ""
        for elem in emploiList:
            if elem in tweet.text:
                poste = elem
                break

        try:
            response = natural_language_understanding.analyze(
                text=tweet.text,
                features=Features(relations=RelationsOptions())).get_result()
            for i in range(len(response['relations'])):
                for j in range(len(response['relations'][i]['arguments'])):
                    if response['relations'][i]['arguments'][j]['entities'][0]['type'] == "Location" or \
                                    response['relations'][i]['arguments'][j]['entities'][0][
                                        'type'] == "GeopoliticalEntity":
                        location = " " + response['relations'][i]['arguments'][j]['entities'][0]['text']
        except:
            continue
        if poste != "" or location != "":
            entry = {'tweet': tweet._json, 'date_publication': str(datetime.datetime.now()), 'poste': poste, 'location': location
                     , 'text': tweet.text}
            print(entry)
            es.index(index='emploi', doc_type='offre', id=Id_enrg, body=entry)
            Id_enrg += 1
