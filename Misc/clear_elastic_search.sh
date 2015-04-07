#!/bin/bash
curl -XDELETE 'http://cmput301.softwareprocess.es:8080/cmput301w15t03/_query' -d '{"query": {"match_all": {}}}'; echo;