from fastapi import APIRouter, Depends, HTTPException, FastAPI
from pydantic import BaseModel


from transformers import AutoTokenizer, AutoModelForCausalLM, BitsAndBytesConfig


app = FastAPI()

class FromClient(BaseModel):
	prompt: str
	db: str

class ToClient(BaseModel):
	result : str
	
################## AI Model Configuration ###############
model_id = "davidkim205/ko-gemma-2-9b-it"

quantization_config = BitsAndBytesConfig(load_in_4bit=True)

tokenizer = AutoTokenizer.from_pretrained(model_id)
model = AutoModelForCausalLM.from_pretrained(
    model_id,
    quantization_config=quantization_config)
##########################################################



@app.post("/AI")
def ai(from_client: FromClient):


	###################### AI Operate ###################
	chat = [
    { "role": "system", "content":"당신은 SQLITE SQL Query문을 만드는 AI입니다."},
    { "role": "user", "content": from_client.db + from_client.prompt + " 니가 만든 쿼리문은 그대로 복사해서 명령어로 사용할거야. 따라서 Query문 자체만 만들어주고, 내 말에 응답하는 문장은 만들지마. 네가 만든 문장이 쿼리문 이라면  쉽게 파싱할 수 있게 문장 맨 앞, 맨 뒤에는 <#s#> 토큰을 달아줘. 최대한 신속하게 만들어줘!" },
	]

	prompt = tokenizer.apply_chat_template(chat, tokenize=False, add_generation_prompt=True)
	inputs = tokenizer.encode(prompt, add_special_tokens=False, return_tensors="pt")
	outputs = model.generate(input_ids=inputs.to(model.device), max_new_tokens=1024)
	result = tokenizer.decode(outputs[0])
	return ToClient(result = result)


