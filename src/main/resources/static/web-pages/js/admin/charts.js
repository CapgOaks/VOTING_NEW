import { getToken } from "../utils/jwtUtils.js";
console.log("charts.js loaded");
console.log("getToken", getToken());

async function renderVotersChart() {
    const ctx = document.getElementById('votersChart').getContext('2d');
    const barCtx = document.getElementById('votersBarChart').getContext('2d');

    try {
        const response = await fetch('/api/elections/voters-count', {
            headers: {
                'Authorization': `Bearer ${getToken()}`
            }
        });

        if (!response.ok) {
            throw new Error(`Error ${response.status}`);
        }

        const data = await response.json();

        const labels = data.map(item => item.electionName);
        const votersCounts = data.map(item => item.votersCount);

        const backgroundColors = [
            'rgba(255, 99, 132, 0.7)',
            'rgba(54, 162, 235, 0.7)',
            'rgba(255, 206, 86, 0.7)',
            'rgba(75, 192, 192, 0.7)',
            'rgba(153, 102, 255, 0.7)',
            'rgba(255, 159, 64, 0.7)'
        ];

        const borderColors = backgroundColors.map(color => color.replace('0.7', '1'));

        // Pie chart
        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Voters Per Election',
                    data: votersCounts,
                    backgroundColor: backgroundColors,
                    borderColor: borderColors,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right',
                    },
                    title: {
                        display: true,
                        text: 'Voters Per Election',
                        color: 'rgb(255, 99, 132)'
                    }
                }
            }
        });

        // Bar chart
        new Chart(barCtx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Voters Count',
                    data: votersCounts,
                    backgroundColor: backgroundColors,
                    borderColor: borderColors,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Voters Count Bar Chart',
                        color: 'rgb(54, 162, 235)'
                    }
                }
            }
        });

    } catch (error) {
        console.error('Error fetching voters count data:', error);
        alert('Failed to load voters count data. Please try again.');
    }
}

renderVotersChart();

export { renderVotersChart };
