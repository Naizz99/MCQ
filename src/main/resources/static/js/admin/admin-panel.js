
var stdCount = document.getElementById("stdCount").textContent;
var pblCount = document.getElementById("pblCount").textContent;
var lecCount = document.getElementById("lecCount").textContent;
var clsCount = document.getElementById("clsCount").value;
var aClsCount = document.getElementById("aClsCount").value;
var paperCount = document.getElementById("paperCount").textContent;
var sessionCount = document.getElementById("sessionCount").textContent;

document.addEventListener("DOMContentLoaded", () => {
	new Chart(document.querySelector('#barChart'), {
		type: 'bar',
		data: {
			labels: ['Registered Students', 'Publishers', 'Registered Lecturers', 'Groups', 'Active Groups', 'Papers', 'All Sessions'],
			datasets: [{
				label: 'Bar Chart',
				data: [stdCount, pblCount, lecCount, clsCount, aClsCount, paperCount, sessionCount],
				backgroundColor: [
					'rgba(255, 99, 132, 0.2)',
                      'rgba(255, 159, 64, 0.2)',
                      'rgba(255, 205, 86, 0.2)',
                      'rgba(75, 192, 192, 0.2)',
                      'rgba(54, 162, 235, 0.2)',
                      'rgba(153, 102, 255, 0.2)',
                      'rgba(201, 203, 207, 0.2)'
				],
				borderColor: [
					'rgb(255, 99, 132)',
                      'rgb(255, 159, 64)',
                      'rgb(255, 205, 86)',
                      'rgb(75, 192, 192)',
                      'rgb(54, 162, 235)',
                      'rgb(153, 102, 255)',
                      'rgb(201, 203, 207)'
				],
				borderWidth: 1
			}]
		},
		options: {
			scales: {
				y: {
					beginAtZero: true
				}
			}
		}
	});
});