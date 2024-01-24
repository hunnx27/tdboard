import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function() {
    
    const id = document.getElementById('facilityId').value
    getDetailId(id)

}

async function getDetailId(id) {
    await useAxios.get(`/api/v1/facilities/${id}`,
    {}
    ,(res)=> {

        const customData = {
            ...res.data,
            startDateText : useFilters().YYYYMMDD(res.data.startDate,'YYYY월 MM월 DD일'),
            endDateText : useFilters().YYYYMMDD(res.data.endDate,'YYYY월 MM월 DD일'),
            applicationStartDateText : useFilters().YYYYMMDD(res.data.applicationStartDate,'YYYY월 MM월 DD일'),
            applicationEndDateText : useFilters().YYYYMMDD(res.data.applicationEndDate,'YYYY월 MM월 DD일')
            
        }
        
        var template = document.getElementById("body-template").innerHTML;
        var result = Mustache.render(template, customData);
        document.getElementById("body-template").innerHTML = result;

      
    },(err)=> {
        console.log('err',err)
    })
}
