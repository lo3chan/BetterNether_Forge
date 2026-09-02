import os
import re
from collections import defaultdict

bclib_imports = defaultdict(list)

for root, _, files in os.walk('src/main/java'):
    for file in files:
        if file.endswith('.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r') as f:
                content = f.read()
                matches = re.findall(r'^import (org\.betterx\.bclib\.[^;]+);', content, re.MULTILINE)
                for match in matches:
                    bclib_imports[match].append(filepath)

# Sort by frequency
sorted_imports = sorted(bclib_imports.items(), key=lambda x: len(x[1]), reverse=True)

for imp, files in sorted_imports[:100]:
    print(f"{len(files)} {imp}")
