#!/bin/bash
./gradlew compileJava --stacktrace --info > build_errors_full.txt 2>&1
grep -E "EntityHydrogenJellyfish|EntityFirefly|EntityFlyingPig" build_errors_full.txt | head -n 10