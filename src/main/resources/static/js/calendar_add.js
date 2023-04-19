/* begin Set today and tomorrow to calendar*/
var date = new Date();
var day = date.getDate();
var month = date.getMonth() + 1;
var year = date.getFullYear();
if (month < 10) month = "0" + month;
if (day < 10) day = "0" + day;
var today = year + "-" + month + "-" + day;
document.getElementById('startDate').value = today;

date.setDate(date.getDate()+1);
day = date.getDate();
month = date.getMonth() + 1;
year = date.getFullYear();
if (month < 10) month = "0" + month;
if (day < 10) day = "0" + day;
var tomorrow = year + "-" + month + "-" + day;
document.getElementById('endDate').value = tomorrow;
/* end Set today and tomorrow to calendar*/


function compare()
{
    var startDt = document.getElementById("startDate").value;
    var endDt = document.getElementById("endDate").value;

    if( (new Date(startDt).getTime() >= new Date(endDt).getTime()))
    {
        alert("Not acceptable!\nFirst date ( " + startDt + " ) is greater than Last date( "+ endDt +" )!");
        document.getElementById('startDate').value = today;
        document.getElementById('endDate').value = tomorrow;
    }
}