function writeimg(imgname,outputname,down)
img = imread(imgname);
thresh = 1;
img_size = size(img);
first_point = 0;
found = 0;
if ( down )
  for i = 1:img_size(1)
    if ( max(max( img(i,:,:)) ) > thresh )
      out = ["d;\n" num2str(i) ";\n"];
      first_point = i;
      break;
    endif
  endfor
  for i = first_point:img_size(1)
    found = 0;
    for j = 1:img_size(2)
      if ( max(img(i,j,:) ) > thresh && found == 0 )
	found = j;
      elseif ( max( img(i,j,:) ) < thresh && found != 0 )
	out = ["" out num2str(found) "+" num2str(j-1-found) ","];
	found = 0;
      endif
    endfor
    if ( strcmp(out(1,size(out)(2)),",") )
      out = [out(:,1:size(out)(2)-1) ":"];
    else
      out = [out ":"];
    endif
  endfor
else
  for i = 1:img_size(2)
    if ( max(max( img(:,i,:)) ) > thresh )
      out = ["r;\n" num2str(i) ";\n"];
      first_point = i;
      break;
    endif
  endfor
  for j = first_point:img_size(2)
    found = 0;
    for i = 1:img_size(1)
      if ( max(img(i,j,:) ) > thresh && found == 0 )
	found = i;
      elseif ( max( img(i,j,:) ) < thresh && found != 0 )
	out = ["" out num2str(found) "+" num2str(i-1-found) ","];
	found = 0;
      endif
    endfor
    if ( strcmp(out(1,size(out)(2)),",") )
      out = [out(:,1:size(out)(2)-1) ":"];
    else
      out = [out ":"];
    endif
  endfor
endif

do 
  l = size(out)(2);
  if ( strcmp(out(1,l-1:l),"::") ) 
    out = [out(:,1:l-1)];
    l = 1;
  else
    l = 0;
  endif
until ( l == 0 )

out = [out ";"];
fid = fopen(outputname,"w");
fdisp(fid,out);
fclose(fid);
endfunction
