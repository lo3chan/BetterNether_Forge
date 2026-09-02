#!/bin/bash
# Review the missing dependencies from the compilation.
cat build_errors.txt | grep "does not exist" | awk '{print $4}' | sort | uniq -c | sort -nr | head -n 30
