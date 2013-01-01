#!/bin/bash

# This creates all the icons for Dominizer
function opti {
    if [ -f $1 ]; then
	echo "Optimizing $1..."
	optipng -quiet -zc1-9 -zm1-9 -zs0-3 -f0-5 $1
    fi
}

function emv {
    if [ -f $1 ]; then
	echo "Moving $1 to -> $2"
	mv $1 $2
    fi
}

rm -rf nokia android

for si in 16 22; do 
    echo "Creates in size ${si}x${si}"
    rm *.png
    gimp -i \
	-b '(dominizer-create "icons.xcf" "ba 0 1 23 24" '${si}')' \
	-b '(dominizer-create "icons.xcf" "in 0 1 21 22 23 24 31" '${si}')' \
	-b '(dominizer-create "icons.xcf" "se 0 1 21 22 23 25" '${si}')' \
	-b '(dominizer-create "icons.xcf" "al 0 1 2 23" '${si}')' \
	-b '(dominizer-create "icons.xcf" "pr 0 2 23 24" '${si}')' \
	-b '(dominizer-create "icons.xcf" "co 0 1 2 23 24" '${si}')' \
	-b '(dominizer-create "icons.xcf" "hi 0 1 2 4 23 24 51 52" '${si}')' \
	-b '(dominizer-create "icons.xcf" "da 0 1 2 4 23 24" '${si}')' \
	-b '(dominizer-create "icons.xcf" "p0 0" '${si}')' \
	-b '(dominizer-create "icons.xcf" "p1 0" '${si}')' \
	-b '(dominizer-create "icons.xcf" "p2 2" '${si}')' \
	-b '(dominizer-create "icons.xcf" "p3 0" '${si}')' \
	-b '(dominizer-create "icons.xcf" "p4 0" '${si}')' \
	-b '(dominizer-create "icons.xcf" "us 0 1 2 21 22 23 24 25 31 41 42 44 45 51 52 55 61 62" '${si}')' \
	-b '(gimp-quit 0)' --verbose
    emv p00.png p0.png
    emv p10.png p1.png
    emv p22.png p2.png
    emv p30.png p3.png
    emv p40.png p4.png
    
    gimp -i \
	-b '(dominizer-create "coins.xcf" "t r0 P0 r1 r2 r3 r4 r5 r6 r7 r8 r9 r10 r11 r12" '${si}')' \
	-b '(dominizer-create "coins.xcf" "tB P0 r2 r3" '${si}')' \
	-b '(dominizer-create "coins.xcf" "tP 1 2 3 4 5 6 7 8 9" '${si}')' \
	-b '(dominizer-create "coins.xcf" "tBP 1" '${si}')' \
	-b '(gimp-quit 0)' --verbose
    for i in `seq 0 16`; do
	emv tr$i.png t$i.png
	emv tBr$i.png tB$i.png
	emv tBP$i.png tB${i}P.png
	emv tP$i.png t${i}P.png
    done

    # Move the pictures to the correct folder...
    if [ $si -eq 16 ]; then
	dir=../resources/img
    else
	dir=../resources/img/22x22
    fi
    mv *.png $dir/
    for png in $dir/*.png ; do
	opti $png
    done
done
