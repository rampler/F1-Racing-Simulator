track = imread('hungaroring.png');
[X Y Z] = size(track);

for i=1:X
    for j=1:Y
        if mod(i,5) == 0
            track(i,j,1) = 0;
            track(i,j,2) = 0; 
            track(i,j,3) = 0; 
        end
        if mod(j,5) == 0
            track(i,j,1) = 0;
            track(i,j,2) = 0; 
            track(i,j,3) = 0; 
        end
    end
end

figure(1), imshow(track);
imwrite(track, 'hungaroring-matlab.jpg', 'jpg');