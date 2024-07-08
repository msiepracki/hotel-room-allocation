# Hotel Room Allocation
## Requirements
Build a room occupancy optimization tool for one of our hotel clients! Our customer has a certain number of free rooms each night, as well as potential guests that would like to book a room for that night.

Our hotel clients have two different categories of rooms: Premium and Economy. Our hotels want their customers to be satisfied: they will not book a customer willing to pay EUR 100 or more for the night into an Economy room. But they will book lower paying customers into Premium rooms if these rooms are empty and all Economy rooms are occupied with low paying customers. The highest paying customers below EUR 100 will get preference for the “upgrade”. Customers always only have one specific price they are willing to pay for the night.

Please build a small API that provides an interface for hotels to enter the numbers of Premium and Economy rooms that are available for the night and then tells them immediately how many rooms of each category will be occupied and how much money they will make in total. Potential guests are represented by an array of numbers that is their willingness to pay for the night.

Use the following raw JSON file/structure as mock data for potential guests in your tests:
```json
[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]
```


## Requirements for a valid solution
* Java Version: Use the most modern version of Java you are comfortable with.
* Version Control: Track your progress through Git commits.
* Documentation: Provide a README explaining how to run the project and tests.
* Testing: Implement tests as specified in the provided test cases.
* Web Framework: Use Spring Boot to expose the API.
  * Make sure the `run.sh` script starts the application. Your script will be executed inside a Docker container (`eclipse-temurin:17-jdk-jammy` - Ubuntu v22.04).
  * Make sure the application starts on port 8080
  * Make sure to implement the REST API as a POST request to `/occupancy` with:
    * Input: `{"premiumRooms": 7, "economyRooms": 5, "potentialGuests": [23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]}`
    * Output: `{"usagePremium": 6, "revenuePremium": 1054, "usageEconomy": 4, "revenueEconomy": 189.99}`
* Code Quality: Maintain a clean code structure and formatting.
  * Use thoughtful naming for variables and functions.
  * Prioritize Quality: Focus on high code quality over completeness if you feel time pressure.
* Provide the URL of your private GitHub repository. Consider using:
  * https://github.blog/2022-10-18-introducing-fine-grained-personal-access-tokens-for-github/
  * https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html

## Tests

### Test 1
```
(input) Free Premium rooms: 3
(input) Free Economy rooms: 3
(output) Usage Premium: 3 (EUR 738)
(output) Usage Economy: 3 (EUR 167.99)
```

### Test 2
```
(input) Free Premium rooms: 7
(input) Free Economy rooms: 5
(output) Usage Premium: 6 (EUR 1054)
(output) Usage Economy: 4 (EUR 189.99)
```

### Test 3
```
(input) Free Premium rooms: 2
(input) Free Economy rooms: 7
(output) Usage Premium: 2 (EUR 583)
(output) Usage Economy: 4 (EUR 189.99)
```
