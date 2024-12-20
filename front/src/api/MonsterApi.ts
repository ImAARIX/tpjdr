import {CharacteristicsResponse} from "./CharacterApi.ts";
import api from "./api.tsx";

export enum MonsterTypeEnum {
    RAT = "RAT",
    WOLF = "WOLF",
    DEMON = "DEMON"
}

export interface InventoryObjectBody {
    id: number;
    inventory: object;
    name: string;
    characteristics: CharacteristicsResponse
}

export interface MonsterResponse {
    id: number;
    type: MonsterTypeEnum;
    health: number;
    droppedObject: InventoryObjectBody;
}

export async function saveNewMonster(monsterType: MonsterTypeEnum): Promise<MonsterResponse|null> {
    const response = await api.get(`/monster/save/${monsterType}`)

    return response?.data;
}

export async function getMonsterById(id: number): Promise<MonsterResponse|null> {
    const response = await api.get(`/monster/${id}`)

    return response?.data;
}

export async function wasAttacked(id: number, username: string): Promise<MonsterResponse | null> {
    const response = await api.put(`/monster/${id}/attacked/${username}`)

    return response?.data;
}

export const monsterWasAttacked = async (monsterId: number): Promise<any> => {
  const response = await fetch(`/api/monster/${monsterId}/attack`, {
    method: 'POST'
  });
  return response.json();
};
