(script-fu-register
    "dominizer-create"                                ;func name
    "Creates all the Dominion images"                 ;menu label
    "Test"              ;description
    "Nick Papior Andersen"                            ;author
    "copyright 2010, Nick Papior Andersen;\
      For the development of Dominizer"               ;copyright notice
    "September 3, 2010"                               ;date created
    ""                                                ;image type that the script works on
    SF-FILENAME    "Image"  "/home/nicpa/programming"
    SF-STRING      "Image names"          "ba 0 1 2 3 4 5 11"       ;a string variable
    ;;SF-FONT        "Font"          "Charter"        ;a font variable
    ;;SF-ADJUSTMENT  "Font size"     '(50 1 1000 1 10 0 1)
                                                      ;a spin-button
    ;;SF-COLOR       "Color"         '(0 0 0)         ;color variable
    SF-ADJUSTMENT  "Image size" '(16 10 100 1 10 1 0)
  )
(script-fu-menu-register "dominizer-create" "<Image>/Image/Dominize")
(define (dominizer-create inFileName inText inImageSize)
  (let*	(
	 (theImage (car (gimp-file-load RUN-NONINTERACTIVE inFileName inFileName)))
	 (baseLayer (car (strbreakup inText " ")))
	 (typeLayer (cdr (strbreakup inText " ")))
	 (layerCount (car (gimp-image-get-layers theImage)))
	 (layerList (cadr (gimp-image-get-layers theImage)))
	 (newImage)
	 (theLayer)
	 (thisType)
	 (newFileName)
	 (drawable)
	 (i layerCount)
	 )
    (gimp-message-set-handler 1)
    (gimp-message "Preparing to save Dominizer images from:")
					;	(gimp-image-resize theImage inImageSize inImageSize 0 0)
    (while (not (null? typeLayer))
	   (set! theImage (car (gimp-file-load RUN-NONINTERACTIVE inFileName inFileName)))
	   (gimp-image-scale-full theImage inImageSize inImageSize INTERPOLATION-LANCZOS)
	   (set! layerCount (car (gimp-image-get-layers theImage)))
	   (set! layerList (cadr (gimp-image-get-layers theImage)))
	   (set! thisType (car typeLayer))
	   (set! newFileName (string-append (string-append baseLayer thisType) ".png"))
	   (gimp-message (string-append "The new filename: " newFileName))
	   (dominizer-unvisible-layers theImage)
	   (set! i layerCount)
	   (while (> i 0)
		  (set! i (- i 1))
		  (set! theLayer (aref layerList i))
;		  (gimp-message (string-append "Checking layer: " (car (gimp-drawable-get-name theLayer) )))
		  (if (= (strcmp (car (gimp-drawable-get-name theLayer)) "background") 0)
		      (begin
			(gimp-message (string-append "Setting TRUE layer: " (car (gimp-drawable-get-name theLayer) )))
			(gimp-drawable-set-visible theLayer TRUE)
			)
		      )
		  (if (= (strcmp (car (gimp-drawable-get-name theLayer)) thisType) 0)
		      (begin
			(gimp-message (string-append "Setting TRUE layer: " (car (gimp-drawable-get-name theLayer) )))
			(gimp-drawable-set-visible theLayer TRUE)
			)
		      )
		  (if (= (strcmp (car (gimp-drawable-get-name theLayer)) baseLayer) 0)
		      (begin
			(gimp-message (string-append "Setting TRUE layer: " (car (gimp-drawable-get-name theLayer) )))
			(gimp-drawable-set-visible theLayer TRUE)
			)
		      )
		  )
	   (gimp-message "Done with the loop, merge")
	   (set! drawable (car (gimp-image-merge-visible-layers theImage EXPAND-AS-NECESSARY)))
	   (gimp-message "Flatten")
;	   (set! drawable (gimp-image-get-active-drawable theImage))
	   (gimp-message "Save file")
;	   (if (not (null? drawable))
;	   	(gimp-message "Is not null!")
;	   	)
	   (file-png-save2 RUN-NONINTERACTIVE theImage drawable newFileName newFileName 0 9 0 0 0 0 0 0 1)
;	   (file-png-save-defaults RUN-NONINTERACTIVE theImage drawable newFileName newFileName)
;	   (gimp-file-save RUN-NONINTERACTIVE theImage drawable newFileName newFileName)
	   (gimp-message "Delete image")
;	   (gimp-image-delete newImage)
	   (gimp-message "Finished opening.")
	   (set! typeLayer (cdr typeLayer))
	   )
    )
  )

(define (dominizer-unvisible-layers inImage)
	(let* (
		(layers (gimp-image-get-layers inImage))
		(num-layers (car layers))
		(layer-array (cadr layers))
		(theLayer)
		)	
	(gimp-message "Unvisible start")
	(gimp-image-undo-group-start inImage)
	(gimp-message "Loop ")
	(while (> num-layers 0)
	       (set! num-layers (- num-layers 1))
	       (set! theLayer (aref layer-array num-layers))
;	       (gimp-message (string-append "The layer: " (car (gimp-drawable-get-name theLayer))))
	       (if (= (car (gimp-drawable-get-visible theLayer) ) TRUE)
		   (gimp-drawable-set-visible theLayer FALSE)
		   )
	)
	(gimp-image-undo-group-end inImage)
;	(gimp-displays-flush)
	(gimp-message "Unvisible finished")
	)
)
(define (dominizer-show-png-defaults)
	(let* (
		(pngInfo (file-png-get-defaults))
		(numInfo 0)
		(intel)
		(comp)
		(bkgd)
		(gama)
		(offs)
		(phys)
		(t)
		(commt)
		(svtrans)
		)	
	(gimp-message "Show-png defaults")
	(file-png-get-defaults intel comp bkgd gama offs phys t commt svtrans)
	(gimp-message (string-append "Info: " pngInfo))
;	(gimp-message (string-append "Interlace: " intel))
;	(gimp-message (string-append "Interlace: " comp))
;	(gimp-message (string-append "Interlace: " bkgd))
;	(gimp-message (string-append "Interlace: " gama))
;	(gimp-message (string-append "Interlace: " offs))
;	(gimp-message (string-append "Interlace: " phys))
;	(gimp-message (string-append "Interlace: " t))
	(gimp-message "Showpng defaults finished")
	)
)

; (dominizer-test "sad" "sad" 15)
(define (dominizer-test inFileName inText inImageSize)
	(let* (
	 	(theImage (car (gimp-file-load RUN-NONINTERACTIVE inFileName inFileName)))
		(typeLayer (cdr (strbreakup inText " ")))
		(thisType)
	     )
	(gimp-message-set-handler 1)
	(gimp-message "Preparing the test to save Dominizer images from:")
	(gimp-message inText)
	(gimp-message inImageSize)
	(while (not (null? typeLayer))
		(set! thisType (car typeLayer))
		(gimp-message thisType)
		(set! typeLayer (cdr typeLayer))
	)
)
)
(script-fu-register
    "dominizer-test"                                ;func name
    "Creates all the Dominion images"                 ;menu label
    "Test"              ;description
    "Nick Papior Andersen"                            ;author
    "copyright 2010, Nick Papior Andersen;\
      For the development of Dominizer"               ;copyright notice
    "September 3, 2010"                               ;date created
    ""                                                ;image type that the script works on
    SF-FILENAME  "Image" ""
    SF-STRING      "Image names"          "ba 0 1 2 3 4 5 11"       ;a string variable
    ;;SF-FONT        "Font"          "Charter"        ;a font variable
    ;;SF-ADJUSTMENT  "Font size"     '(50 1 1000 1 10 0 1)
                                                      ;a spin-button
    ;;SF-COLOR       "Color"         '(0 0 0)         ;color variable
    SF-ADJUSTMENT  "Image size" '(16 10 100 1 10 1 0)
  )
