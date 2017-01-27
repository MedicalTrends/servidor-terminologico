<%@ Page Title="" Language="C#" MasterPageFile="~/Servicios.Master" AutoEventWireup="true" CodeBehind="ObtenerTerminosPedibles.aspx.cs" Inherits="MinsalWS.Pages.Servicio2.ObtenerTerminosPedibles" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeadContent" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <div style="width:100%;">
    <table style="width:100%;">
        <tr>
            <td>
                <asp:Label ID="lblNombbreCategoria" Text="Nombre categoría:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtNombreCategoria" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblIdEstablecimiento" Text="Establecimiento id:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtIdEstablecimiento" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblNombreRefSet" Text="Nombre ref set:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtNombreRefSet" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblPedible" Text="Pedible:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtPedible" Text="" runat="server"></asp:TextBox> <asp:Label ID="lblNota" Text="True o False" runat="server"></asp:Label>
            </td>
        </tr>
        <tr>
            <td>
                
            </td>
            <td>
                <asp:Button ID="btnInvocar" Text="Invocar" runat="server" OnClick="btnInvocar_Click"></asp:Button>
            </td>
        </tr>
        </table>
        <asp:TextBox id="TextArea1" TextMode="multiline" Columns="50" Rows="5" runat="server" Width="100%"/>
    </div>

</asp:Content>
