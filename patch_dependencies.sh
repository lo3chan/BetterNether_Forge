sed -i 's/2024.08.18/2024.11.17/g' betternether.gradle
cat << 'INNEREOF' >> betternether.gradle

dependencies {
    compileOnly "dev.emi:emi-neoforge:1.1.9+1.21.1:api"
    compileOnly "vazkii.patchouli:Patchouli:1.21-87-NEOFORGE:api"
    compileOnly "mezz.jei:jei-1.21.1-neoforge-api:19.21.0.23"
}
INNEREOF
