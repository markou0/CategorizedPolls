
var randomScalingFactor = function() {
    return Math.round(Math.random() * 100);
};

var color = Chart.helpers.color;
var colorNames = Object.keys(window.chartColors);

var barConfig = {
    type: 'horizontalBar',
    data: {
        labels: [],
        datasets: [{
            label: 'All Respondents',
            data: [],
        },]
    },
    options: {
        title: {
            display: true,
            text: 'Answers Rating'
        },
        scales: {
            yAxes: [{
                ticks: {
                    callback: function (value) { if (Number.isInteger(value)) { return value; } },
                    stepSize: 1,
                    beginAtZero:true
                }
            }]
        }
    }
};

var pieConfig = {
    type: 'pie',
    data: {
        datasets: [{
            data: []
        }],
        labels: []
    },
    options: {
        responsive: true,
        title: {
            display: true,
            text: 'Respondents Categorized'
        }
    }
};

var radarConfig = {
    type: 'radar',
    data: {
        labels: [],
        datasets: []
    },
    options: {
        legend: {
            position: 'top',
        },
        title: {
            display: true,
            text: 'Categories Votes Comparison'
        },
        scale: {
          ticks: {
            beginAtZero: true
        }
    }
}
};

$( document ).ready( function(){
    window.barChartDomEl = $("[name='chart-bar']").clone();
    window.pieChartDomEl = $("[name='chart-pie']").clone();
    window.radarChartDomEl = $("[name='chart-radar']").clone();
    $("[name^='chart']").remove();
    window.barCharts = [];
    window.pieCharts = [];
    window.radarCharts = [];

    ajax_getPollResults();

    function createCharts(pollResult){
       /* pollResult = {
            categoryRatingList : [{ label: "A", rating: 2}, { label: "B", rating: 10}, { label: "C", rating: 6}],
            questionResultList : [
            { 
                answerRatingList: [{ label: "A", rating: 2}, { label: "B", rating: 10}, { label: "C", rating: 6}] ,
                answerRatingByCategoryList : 
                [
                {category : "cat4", ratings : [{ label: "A", rating: 2}, { label: "B", rating: 10}, { label: "C", rating: 6}]},
                {category : "cat1", ratings : [{ label: "A", rating: 5}, { label: "B", rating: 0}, { label: "C", rating: 3}]},
                {category : "cat4", ratings : [{ label: "A", rating: 1}, { label: "B", rating: 18}, { label: "C", rating: 5}]}
                
                ]
            }
            ]
        }*/


        $("[name^='chart']").remove();

        var newPieChartDomEl = pieChartDomEl.clone();
        $('[name="categories-ratings-container"] .canvas-holder').append(newPieChartDomEl);
        window.pieCharts[0] = new Chart($(newPieChartDomEl), pieConfig);
        configurePieChart(window.pieCharts[0], pollResult)






        var barChartsContainers = $('[name="poll-answer-ratings-container"] .canvas-holder');
        var radarChartsContainers = $('[name="categorized-poll-answer-ratings-container"] .canvas-holder');
        for (var i = 0; i < barChartsContainers.length; i++) {
            var newBarChartDomEl = window.barChartDomEl.clone();
            var newRadarChartDomEl = window.radarChartDomEl.clone();
            $(barChartsContainers).eq(i).append(newBarChartDomEl);
            $(radarChartsContainers).eq(i).append(newRadarChartDomEl);
            window.barCharts[i] = new Chart($(newBarChartDomEl), barConfig);
            window.radarCharts[i] = new Chart($(newRadarChartDomEl), radarConfig);
            configureBarChart( window.barCharts[i] , pollResult, i)
            configureRadarChart(window.radarCharts[i], pollResult, i)
        }

    }






    function updateChart(chart, $datasets, $labels){
        chart.config.data.datasets = JSON.parse(JSON.stringify($datasets))
        chart.config.data.labels = JSON.parse(JSON.stringify($labels))
        chart.update();
    };

    function generateDatasets(chart, $labels, $datasets){
        var datasets = []
        var dataset = { borderWidth: 3, data:[]};
        for (var i = 0; i < $datasets.length; i++) {
            var newDataset = JSON.parse(JSON.stringify(dataset))
            newDataset.data = $datasets[i].data;
            datasets.push(newDataset);
        }

        var colors = generateColors(datasets[0].data.length, 0.2);
        solidColors = []
        colorsWithAlfa = []

        for(var color of colors){
            solidColors.push(color.solid)
            colorsWithAlfa.push(color.withAlfa)
        }

        for (var i = 0; i < datasets.length; i++) {

            if($labels)datasets[i].label = $labels[i];
            if(chart.config.type == 'radar'){
                datasets[i].backgroundColor = colors[i].withAlfa
                datasets[i].hoverBackgroundColor = colors[i].solid
                datasets[i].borderColor = colors[i].solid
                datasets[i].pointBorderColor = colors[i].solid
            }
            else {
                datasets[i].backgroundColor = chart.config.type == 'pie'?solidColors:colorsWithAlfa
                datasets[i].hoverBackgroundColor = solidColors
                datasets[i].borderColor = solidColors
                datasets[i].pointBorderColor = solidColors   
            }
        }
        console.log(datasets)
        return datasets;
    }

    /*function generateDatasets(chart, $data, $labels){
        var datasets = []
        var dataset = { borderWidth: 1, data:[]};
        datasets.push(dataset);

        for (var i = 0; i < datasets.length; i++) {
            if($labels)datasets[i].label = $labels[i];
            for (var data of $data) { 
                datasets[i].data.push(data);
            }
            datasets[i].backgroundColor = generateColors(datasets[i].data.length, chart.config.type == 'pie'?1:0.2)
            datasets[i].hoverBackgroundColor = generateColors(datasets[i].data.length, chart.config.type == 'pie'?1:0.2)
            datasets[i].borderColor = generateColors(datasets[i].data.length)
            datasets[i].pointBorderColor = generateColors(datasets[i].data.length)
        }

        return datasets;
    }*/

    function configurePieChart(chart, pollResult){
        var categoryRatings = pollResult.categoryRatingList;
        var datasets = []
        var dataset = {}
        dataset.data = []
        dataset.labels = []
        datasets.push(dataset)

        for (var rating of categoryRatings) {
            dataset.data.push(rating.rating);
            dataset.labels.push(rating.label);
        }
        updateChart(chart, generateDatasets(chart, dataset.labels, datasets), dataset.labels)
    }

    function configureBarChart(chart, pollResult, questionIndex){
        var answerRatings = pollResult.questionResultList[questionIndex].answerRatingList;
        var datasets = []
        var dataset = {}
        dataset.data = []
        dataset.labels = []
        datasets.push(dataset)

        for (var rating of answerRatings) {
            dataset.data.push(rating.rating);
            dataset.labels.push(rating.label);
        }
        updateChart(chart, generateDatasets(chart, ["All Respondents Votes"], datasets), dataset.labels)
    }

   /* function configureBarChart(chart, pollResult, questionIndex){
        var answerRatings = pollResult.questionResultList[questionIndex].answerRatingList;
        var data = []
        var labels = []
        for (var rating of answerRatings) {
            data.push(rating.rating);
            labels.push(rating.label);
        }
        var datasets = generateDatasets(chart, data, ["All Respondents"])
        updateChart(chart, datasets, labels)
    }*/

    function configureRadarChart(chart, pollResult, questionIndex){
       var answerRatings = pollResult.questionResultList[questionIndex].answerRatingList;
       var answerLabels = []
       for (var rating of answerRatings) {
        answerLabels.push(rating.label);
    }


    /*    var categoryRatings = pollResult.categoryRatingList;*/
    var categoryLabels = []
/*    for (var rating of categoryRatings) {
        categoryLabels.push(rating.label);
    }
    */

    var answerRatingByCategoryMap = pollResult.questionResultList[questionIndex].answerRatingByCategoryMap;
    console.log(answerRatingByCategoryMap)
    
    var datasets = []

    Object.keys(answerRatingByCategoryMap).forEach(function(key) {
        value = answerRatingByCategoryMap[key];
        console.log(key);
        console.log(value);

        var dataset = {}
        dataset.data = []

        categoryLabels.push(key);
        for(var rating of value){
           dataset.data.push(rating.rating);
       }

       if(dataset.data.length > 0)datasets.push(dataset)

   });

   /* for (var ratingMap of answerRatingByCategoryMap) {
        var dataset = {}
        dataset.data = []
        if(ratingMap.ratings&&ratingMap.ratings.length>0){
            categoryLabels.push(ratingMap.category);
            for(var rating of ratingMap.ratings){
               dataset.data.push(rating.rating);
           }
       }

       if(dataset.data.length > 0)datasets.push(dataset)
   }*/
updateChart(chart, generateDatasets(chart, categoryLabels, datasets), answerLabels)
}

$("#update-charts").click(function(){
    ajax_getPollResults()
}
)


function ajax_getPollResults() {

    $.ajax({
        type : "GET",
        contentType : "text/plain",
        url : "results/getJson",
        dataType : 'json',
        timeout : 100000,

        success : function(pollResult) {
            console.log("SUCCESS: ", pollResult);
            createCharts(pollResult)
        }
        ,
        error : function(e) {
            console.log("ERROR: ", e.responseText);
        }

    })
}
});



function generateColors(amount, alfa = 1){
  var colors = []
  colors.push
  colors.solidColors = []
  colors.colorsWithAlfa = []
  for (var index = 0; index < (amount > colorNames.length ?amount:colorNames.length); index ++) {
    var colorPalette = {}
    colors.push(colorPalette);
    var colorName = colorNames[ index % colorNames.length];;
    var newColor = window.chartColors[colorName];
    colorPalette.solid = color(newColor).rgbString();
    colorPalette.withAlfa = color(newColor).alpha(alfa).rgbString();
}
colors.sort(function(a,b) {
    return Math.random()*Math.random() -  Math.random()*Math.random()
});
return colors
}



