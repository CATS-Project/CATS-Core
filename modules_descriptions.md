## Event detection
### Description
Event detection consists in automatically identifying and describing (temporally and textually speaking) events reported in tweets. This module relies on <a href="http://mediamining.univ-lyon2.fr/people/guille/mabed.php" target="_blank">MABED</a> (i.e. Mention-Anomaly-Based Event Detection).

### Parameters
- Endpoint: http://mediamining.univ-lyon2.fr:5000/detect_events
- Top k events (e.g. 10): number
- Time-slice length in minutes (e.g. 30, 60 or 720): number
- Theta (default to 0.5): number
- Sigma (default to 0.5): number

## Topic modeling
### Description
Topic modeling is a way of automatically discovering hidden themes, i.e. topics, that pervade a collection of tweets. Topic models can help organizing, understanding, and summarizing large amounts of tweets. This module relies on the <a href="http://mediamining.univ-lyon2.fr/people/guille/tom.php" target="_blank">TOM</a>.

### Parameters
- Endpoint: http://mediamining.univ-lyon2.fr:5000/infer_topics
- Number of topics (e.g. 10, 15 or 20): number
- Model (either NMF or LDA): text
- Language (e.g. english or french): text

## Vocabulary
### Description
The vocabulary is the set of all the words (with their frequency) that appear in the corpus.

### Parameters
- Endpoint: http://mediamining.univ-lyon2.fr:5000/vocabulary

## Cloud visualization
### Description
Cloud visualization is a graphical representation of word frequency.

### Parameters
- Endpoint: http://mediamining.univ-lyon2.fr:3000
