import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function() {
    
    const educationId = document.getElementById('educationId').value
    getEducationId(educationId)

}

async function getEducationId(educationId) {
    await useAxios.get(`/api/v1/educations/${educationId}`,
    {}
    ,(res)=> {

        const customData = {
            ...res.data,
            startDateText : useFilters().YYYYMMDD(res.data.startDate,'YYYY월 MM월 DD일'),
            endDateText : useFilters().YYYYMMDD(res.data.endDate,'YYYY월 MM월 DD일'),
            applicationStartDateText : useFilters().YYYYMMDD(res.data.applicationStartDate,'YYYY월 MM월 DD일'),
            applicationEndDateText : useFilters().YYYYMMDD(res.data.applicationEndDate,'YYYY월 MM월 DD일')
            
        }
        
        var template = document.getElementById("educations-template").innerHTML;
        var result = Mustache.render(template, customData);
        document.getElementById("educations-template").innerHTML = result;

      
    },(err)=> {
        console.log('err',err)
    })
}
