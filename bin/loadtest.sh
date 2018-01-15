curdate=$(date '+%Y-%m-%d-%H-%M-%S')
folder="/home/ubuntu/reports/$curdate"
mkdir "$folder"
mkdir "$folder/report"
./jmeter.sh -n -t /home/ubuntu/performance-testing/jmeter/chat.jmx -l "$folder/test.log" -e -o "$folder/report"
zip -r "$folder/report.zip" "$folder/report"
