$(document).ready(function(){
  $(".button-collapse").sideNav();
  $('select').material_select();
});

var conversion={
  'rice':30,
  'wheat':10,
  'maize':20,
  'crop':40,
  'amp':100,
  'ams':120,
  'urea':110,
  'pn':90
}

var stConv={
  'Rajasthan':100,
  'West Bengal':200,
  'Bihar':300,
  'Uttar Pradesh':400
}

var cropVid={
  'Wheat':'https://www.youtube.com/embed/tdPDE_gcyzc',
  'Rice':'https://www.youtube.com/embed/qy3IJKGna_Q',
  'Ladyfinger':'https://www.youtube.com/embed/vD95tkwkKZ0',
  'Millet':'https://www.youtube.com/embed/mfETzrXqVjY' ,
  'Potato':'https://www.youtube.com/embed/GW4ut62-evY' ,
  'Muskmelon':'https://www.youtube.com/embed/h8FnRXso-l0' ,
  'Tomato':'https://www.youtube.com/embed/Az0cbup9QJg' ,
  'Brinjal':'https://www.youtube.com/embed/3Qdha2kVoNY' ,
  'Castor':'https://www.youtube.com/embed/FFj5W_xNmF0' ,
  'Cauliflower':'https://www.youtube.com/embed/ppf34-3Dxog',
  'Mango':'https://www.youtube.com/embed/gzoMWlEHj2c' ,
  'Onion':'https://www.youtube.com/embed/s5TwW88_J7c' ,
  'Banana':'https://www.youtube.com/embed/4OMu_Z3xNEg' ,
  'Cucumber':'https://www.youtube.com/embed/N9ZPHiHf_6k', 
  'Carrot':'https://www.youtube.com/embed/gr00oG-mQNY' ,
  'Guava':'https://www.youtube.com/embed/7Y-N9uZq95g' ,
  'Pea':'https://www.youtube.com/embed/sWhd0jTSHWw' ,
  'Pomegranate':'https://www.youtube.com/embed/TWI_girwP14' ,
  'Sugarcane':'https://www.youtube.com/embed/rXEvBsNXR5g' ,
  'Soyabean':'https://www.youtube.com/embed/ZR0OGwBy5LQ' ,
  'Wood Apple':'https://www.youtube.com/embed/5hQhn_dIlcs' 
}

var stConv1={
  'Raj':{crops:['Wheat','Rice','Ladyfinger'],soil:'A'},
  'WB':{crops:['Rice','Brinjal','Guava'],soil:'B'},
  'Bihar':{crops:['Millet','Onion','Potato'],soil:'B-C'},
  'UP':{crops:['Tomato','Muskmelon','Cucumber'],soil:'D-A'},
  'AP':{crops:['Wood Apple', 'Castor','Carrot'],soil:'E'},
}

function changedCrop(){
  $("#price").val(conversion[$("#crop-name").val()]*$("#amt").val());
}

function changedState(){
  var mCrop = stConv1[$("#state").val()].crops;
  // $("#area").val(stConv1[$("#state").val()].crops + " " + mCrop[0] + " " + cropVid[mCrop[0]]);
  $("#st").val(stConv1[$("#state").val()].soil);
  $("#v1").attr('src',cropVid[mCrop[0]]);
  $("#v2").attr('src',cropVid[mCrop[1]]);
  $("#v3").attr('src',cropVid[mCrop[2]]);
}

function areaChanged(){
  var area=Number($("#area").val());
}