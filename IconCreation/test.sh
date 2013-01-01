#!/bin/sh

# This creates all the icons for Dominizer
echo "Creates in size 16x16"
rm *.png

gimp -i -b '(dominizer-create "coins.xcf" "tP 1 2 3 4 5 6 7 8 9" 16)' -b '(gimp-quit 0)' --verbose
