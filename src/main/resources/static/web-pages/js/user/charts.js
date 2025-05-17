console.log("charts");
const barCtx = document.getElementById('barChart').getContext('2d');

// Sample data for bar chart
const barChart = new Chart(barCtx, {
	type: 'bar',
	data: {
		labels: ['Party A', 'Party B', 'Party C', 'Party D'],
		datasets: [{
			label: 'Votes',
			data: [12, 19, 3, 5],
			backgroundColor: [
				'rgba(255, 99, 132, 0.7)',
				'rgba(54, 162, 235, 0.7)',
				'rgba(255, 206, 86, 0.7)',
				'rgba(75, 192, 192, 0.7)'
			],
			borderColor: [
				'rgba(255, 99, 132, 1)',
				'rgba(54, 162, 235, 1)',
				'rgba(255, 206, 86, 1)',
				'rgba(75, 192, 192, 1)'
			],
			borderWidth: 1
		}]
	},
	options: {
		responsive: true,
		scales: {
			y: {
				beginAtZero: true
			}
		}
	}
});

