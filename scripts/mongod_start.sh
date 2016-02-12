#!/bin/bash
mongod --fork --dbpath $EVA_HOME/data/mongodb --logpath $EVA_HOME/logs/mongod.log --logappend
