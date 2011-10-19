#!/bin/bash
#
#Planetary: 6124_Classic, propose the app for finding keys!
#b0mber: LG Arena KM900, propose the app for finding keys!
#NoMasOvejas: Just problems
#OmegaII: N78
#hali: N95
#Natalor: N82,Nokia/3220
#Tonilia: N70
#Serigala: E71
#fireangel: E90 Comm


#Planetary,NoMasOvejas,OmegaII,hali,b0mber,Natalor,Tonilia,Serigala,fireangel

zip -9 -r dominizer.zip dist/*.ja?

mv dist/Nokia-6120_classic-Dominizer-en.jar dist/Planetary_Classic.jar
mv dist/Nokia-6120-Dominizer-en.jar dist/Planetary.jar
mv dist/Nokia-E71-Dominizer-en.jar dist/Serigala.jar
mv dist/Nokia-E90_Communicator-Dominizer-en.jar dist/fireangel.jar
mv dist/Nokia-N70-Dominizer-en.jar dist/Tonilia.jar
mv dist/Nokia-N78-Dominizer-en.jar dist/OmegaII.jar
mv dist/Nokia-3220-Dominizer-en.jar dist/Natalor3220.jar
mv dist/Nokia-N82-Dominizer-en.jar dist/NatalorN82.jar
mv dist/Nokia-N95-Dominizer-en.jar dist/hali.jar

mv dist/Nokia-6120_classic-Dominizer-en-debug.jar dist/Planetary_Classic-debug.jar
mv dist/Nokia-6120-Dominizer-en-debug.jar dist/Planetary-debug.jar
mv dist/Nokia-E71-Dominizer-en-debug.jar dist/Serigala-debug.jar
mv dist/Nokia-E90_Communicator-Dominizer-en-debug.jar dist/fireangel-debug.jar
mv dist/Nokia-N70-Dominizer-en-debug.jar dist/Tonilia-debug.jar
mv dist/Nokia-N78-Dominizer-en-debug.jar dist/OmegaII-debug.jar
mv dist/Nokia-3220-Dominizer-en-debug.jar dist/Natalor3220-debug.jar
mv dist/Nokia-N82-Dominizer-en-debug.jar dist/NatalorN82-debug.jar
mv dist/Nokia-N95-Dominizer-en-debug.jar dist/hali-debug.jar
zip -9 -j dominizer.zip dist/*.jar