package good.stuff.myapplication.factory

import android.content.Context
import good.stuff.myapplication.dao.GameSqlHelper

fun getNasaRepository(context: Context?) = GameSqlHelper(context)