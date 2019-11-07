# FW-Assignment
1.We take 4 parameters as input Folder Path, Input File Base Name, Output File Base Name, Max File Size.
2. The utility will read all files in the Folder Path that begin with the Input File
Base Name, and process them in increasing order of the number added as a
suffix to each file (1,2,3,....,12,13,...).
3. The utility will ensure that the output files are named using the Output File
Base Name as a prefix, and a counter as a suffix.
4. Merged files will never be greater than Max File Size.
5. Each output file will contain a proper JSON array.
6. Time Complexity: O(nlogn)
7. It was tested on Mac OS and will work fine on others as well.

