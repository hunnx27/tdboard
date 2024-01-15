
// keyup event 
function textfieldChange(use,e){
	  let value = use.value;
    let textfield = use.parentNode;
    let textfieldStatus;
    if(value != ''){
    	textfield.setAttribute('data-status','active');
		  textfieldStatus = textfield.getAttribute('data-status');
        
    }else{
        textfield.setAttribute('data-status','default');
        textfieldStatus = textfield.getAttribute('data-status');
    };
    return
}
// select box
function selectboxEvent(){
  let selectAll = document.querySelectorAll('.select');
  // all select close
  function allSelectClose(){
    selectAll.forEach((all)=>{
      all.classList.remove('on');
      all.querySelector('.option-box').style.height = 0;
      if(all.querySelector('.select-value').getAttribute('data-defailt-text') == all.querySelector('.select-value').innerText){
        all.setAttribute('data-status','default');
      };
    });
  };

  // select-btn click 셀렉트박스 on off
  function selectClick(use,btn,status){
    let optionBox = use.querySelector('.option-box');
    let optionAll = use.querySelectorAll('.option');
    if(use.classList.contains('on') == false){
      allSelectClose();
      use.classList.add('on');
      btn.parentNode.setAttribute('data-status','active');
      optionAll.forEach((option)=>{
        option.disabled = false;
      });
      optionBox.style.height = (Number(getComputedStyle(optionAll[0]).height.replace('px','')) * optionAll.length) + 4 + 'px';
    }else{
      use.classList.remove('on');
      if(use.querySelector('.select-value').getAttribute('data-defailt-text') == use.querySelector('.select-value').innerText){
        btn.parentNode.setAttribute('data-status','default');
      }else{
        btn.parentNode.setAttribute('data-status','confirm');
      };
      optionAll.forEach((option)=>{
        option.disabled = true;
      });
      optionBox.style.height = 0;
    }
    // option click 시 이벤트
    optionAll.forEach((option) =>{
      option.addEventListener('click',()=>{
        //optionClick(option,option.querySelector('span').textContent)
        use.querySelector('.select-value').innerText = option.querySelector('span').innerText
        use.setAttribute('data-status','confirm');
        use.classList.remove('on');
        optionBox.style.height = 0;
      })
    })
    
  }

  // 모든 select 이벤트 on
  selectAll.forEach((select) => {
      let selectBtn = select.querySelector('.select-btn')
      selectBtn.addEventListener('click',()=>{
        let thisStatus = selectBtn.parentNode.getAttribute('data-status');
        selectClick(select,selectBtn,thisStatus);
      });
  });
  // 다른 타겟 클릭시 off
  /*
  document.addEventListener('click',function(e){
    let documentSelectAll = selectAll;
    console.log(e.target);
    documentSelectAll.forEach((documentSelect)=>{
      if(documentSelect.has(e.target).length === 0){
        documentSelect.classList.remove('on');
      };
    });
  });
  */
};
selectboxEvent();

// btn active
function genderActive(use){
  let value = use.getAttribute('data-status');
  let btn = use.parentNode.querySelectorAll('.btn-gender');
  btn.forEach((index)=>{
    index.setAttribute('data-status','default');
  })
  use.setAttribute('data-status','active');
}

// header event 
function headerEvent(){
  let header = document.querySelector('header')


}


/* tab function */
function tab(usetab) {
  let tab = document.getElementsByClassName(usetab);
  let tabWrap = document.querySelectorAll('.tab');
  let tabList = tab[0].querySelectorAll('li');
  let tabCon = tab[0].parentNode.querySelectorAll('.tab-con');

  function tabOff() {
    tabList.forEach((tabremove) => {
      tabremove.classList.remove('on');
    });
    tabCon.forEach((tabconremove) => {
      tabconremove.classList.remove('on');
    });
  }
  tabList.forEach((tabLi, index) => {
    tabLi.addEventListener('click', function (e) {
      let nodes = e.target.closest('.tab');
      let tabthis = e.target;

      if (e.target.classList.contains('on')) {
        tabthis.classList.add('on');
      } else if (e.target.parentNode.classList.contains('on')) {
        tabthis.parentNode.classList.add('on');
      } else if (e.target.classList.contains('on') == false) {
        tabOff();
        tabList[index].classList.add('on');
        tabCon[index].classList.add('on');
      } else if (e.target.parentNode.classList.contains('on') == false) {
        tabOff();
        tabList[index].classList.add('on');
        tabCon[index].classList.add('on');
      }
    });
  });
}
/* selectCustom function */
function selectCustom() {
  var labels = document.querySelectorAll('.dropdown');
  for (let index = 0; index < labels.length; index++) {
    let options = labels[index].querySelectorAll('.dropdown-toggle');
    let optionsList = labels[index].querySelectorAll('li');
    labels[index].addEventListener('click', function () {
      if (labels[index].classList.contains('on')) {
        labels[index].classList.remove('on');
      } else {
        labels.forEach(function (lab) {
          lab.classList.remove('on');
        });
        labels[index].classList.add('on');
        labels[index].focus();
      }
    });

    labels[index].querySelectorAll('li').forEach(function (lablesLi) {
      lablesLi.addEventListener('click', function (e) {
        let textVal = lablesLi.textContent;
        lablesLi.parentNode.previousElementSibling.textContent = textVal;
      });
    });
  }
  document.addEventListener('click', function (e) {
    if (
      e.target.parentNode.classList.contains('dropdown') == false &&
      e.target.classList.contains('dropdown') == false
    ) {
      labels.forEach(function (labelThis) {
        labelThis.classList.remove('on');
      });
    }
  });
}
// textareaHeight()  -- textarea 텍스트 추가 자동크기변경
function textareaHeight() {
  const DEFAULT_HEIGHT = 32; // textarea 기본 height
  var textareaNode = document.querySelectorAll('textarea');
  textareaNode.forEach(function (textarea, idx) {
    textarea.style.height = 0;
    textarea.style.height = 2 + textarea.scrollHeight + 'px';
    textarea.addEventListener('keyup', function (e) {
      var $target = textareaNode[idx];
      $target.style.height = 0;
      $target.style.height = 2 + $target.scrollHeight + 'px';
    });
  });
}
/* all checked */
/*
function boardAllChecked(boardcheck) {
 
  console.log(boardcheck);
  var useTable = boardcheck.closest('.table');
  var useTableBody = useTable.querySelector('tbody');
  var checkboxs = useTableBody.querySelectorAll('input[type="checkbox"]');

  if (boardcheck.checked == true) {
    checkboxs.forEach(function (checkbox) {
      console.log(checkboxs.length);
      checkbox.checked = true;
    });
  } else {
    checkboxs.forEach(function (checkbox) {
      checkbox.checked = false;
    });
  }
  var checkBoxTables = document.querySelectorAll('.table');
  checkBoxTables.forEach(function (checkBoxTable) {
    checkBoxTable.querySelectorAll('input[type="checkbox"]').forEach(function (checkBoxCellAll) {
      checkBoxCellAll.addEventListener('click', function () {
        boardAllChecked(checkBoxCellAll);
      });
    });
  });
}*/
function includeHTML() {
  let z, elmnt, file, xhttp;

  z = document.getElementsByTagName('*');

  for (let i = 0; i < z.length; i++) {
    elmnt = z[i];
    file = elmnt.getAttribute('data-include');

    if (file) {
      xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
          if (this.status == 200) {
            elmnt.innerHTML = this.responseText;
          }
          if (this.status == 404) {
            elmnt.innerHTML = 'Page not found.';
          }
          /* Remove the attribute, and call this function once more: */
          elmnt.removeAttribute('data-include');
          includeHTML();
        } //if
      }; //onreadystatechange

      xhttp.open('GET', file, true);
      xhttp.send();
      return;
    } //if - file
  } //for
} //includeHTML
includeHTML();
window.onload = function () {
  selectCustom();

};
