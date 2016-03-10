def parse():
  with open('cities.txt') as f:
    count = 0
    for line in f:
      temp = line.strip('\n')
      temp = temp.split(" | ")
      coord = temp[0].strip(' ')
      this = coord.split('  ')
      print(this[0] + ',' + this[1] + ',' + temp[1] + ',' + temp[2] + ',' + temp[3])
      #count = count+1
    #print(count)


  
def findStates():
  with open('cities.csv') as f:
    for line in f:
      temp = line.strip('\n')
      temp = line.strip
      print(temp)

parse()