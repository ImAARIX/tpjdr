import api from './api';
import {MonsterTypeEnum} from "./MonsterApi";

export enum ClassTypeEnum {
    WARRIOR = "WARRIOR",
    MAGE = "MAGE",
    POOR = "POOR"
}

export interface CharacterRegisterRecordBody {
    username: string;
    classType: ClassTypeEnum;
}

export interface CharacterResponse {
    id: number;
    username: string;
    classType: ClassTypeEnum;
    inventory: object;
    health: number;
}

export interface CharacteristicsResponse {
    id: number;
    strength: number;
    intelligence: number;
    luck: number;
}

interface WasAttackedBody {
    username: string;
    monsterType: MonsterTypeEnum;
}

export async function registerCharacter(character: CharacterRegisterRecordBody): Promise<CharacterResponse|null> {
    const response = await api.post('/character', character);

    return response?.data
}

export async function getCharacter(username: string): Promise<CharacterResponse|null> {
    const response = await api.get(`/character/${username}`);

    return response?.data
}

export async function existsCharacter(username: string): Promise<boolean|null> {
    const response = await api.get(`/character/${username}/exists`);

    return response?.data
}

export async function getCharacteristics(username: string): Promise<CharacteristicsResponse|null> {
    const response = await api.get(`/character/${username}/characteristics`);

    return response?.data
}

export async function wasAttacked(wasAttackedBody: WasAttackedBody): Promise<CharacterResponse|null> {
    const response = await api.put(`/character/${wasAttackedBody.username}/attacked/${wasAttackedBody.monsterType.toString()}`);

    return response?.data
}

export const characterWasAttacked = async (username: string): Promise<any> => {
  const response = await fetch(`/api/character/${username}/attack`, {
    method: 'POST'
  });
  return response.json();
};
