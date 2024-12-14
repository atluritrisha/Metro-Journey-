# Metro Journey Planner

## Usage
The main menu provides the following options:

1. **Display the Metro Map**  
   Shows each station and its connected neighbors along with distances.

2. **Display the Stations**  
   Lists all metro stations, organized by line.

3. **Compute the Trip Details**  
   - Enter stations by serial number or name.
   - View trip details such as distance, time, cost, and interchanges.

4. **Exit**  
   Ends the program.

---

## Structure and Key Classes

### GraphM
Handles the metro network's graph structure:

- **Vertex**: Represents a station.
- **addVertex()** and **removeVertex()**: Adds or removes a station.
- **addEdge()** and **removeEdge()**: Connects or disconnects two stations with a specified distance.
- **displayMap()**: Displays the complete metro map.
- **hasPath()** and **getMinimumDistance()**: Checks paths and computes shortest distances between stations.

---

## Algorithms

- **Breadth-First Search (BFS)**: Determines if a path exists between stations.
- **Dijkstra’s Algorithm**: Calculates the shortest path distance between stations.

---

## Cost and Time Calculation

- **Cost**: Based on the distance between stations, with specific rates for defined ranges.
- **Time**: Estimated assuming an average speed of 109 km/hr.

---

## Example Usage

1. Start the application.
2. Choose **Compute Trip Details** from the menu.
3. Enter the source and destination stations (either by serial number or name).
4. View the computed route details, including:
   - Distance
   - Time
   - Cost
   - Interchanges

---

## File Structure

```bash
├── Main.java    # Main entry point of the program 
├── GraphM.java  # Contains logic for the metro map graph 
├── stations.csv # List of metro stations 
├── edges.csv    # Edges between stations with distances
```

---

## Sample Data

### `stations.csv`
Contains station names in the format: `<StationName>~<LineCode>`

### `edges.csv`
Contains connections in the format: `<Station1>~<LineCode>,<Distance>`
