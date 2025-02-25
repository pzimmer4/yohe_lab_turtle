#let's learn how to scrape a website. Specifically for turtle data
#setup the environment
import requests
from bs4 import BeautifulSoup

url = "https://www.example.com"  # Replace with the actual URL
response = requests.get(url)

if response.status_code == 200:
    html_content = response.content
else:
    print(f"Error: Failed to fetch URL. Status code: {response.status_code}")
    exit() 